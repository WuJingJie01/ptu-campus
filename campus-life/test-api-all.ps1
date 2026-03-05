# ========================================
# Campus Life API 测试脚本
# ========================================

$BaseUrl = "http://localhost:10001"
$Results = @()

# 辅助函数：发送请求并记录结果
function Test-API {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [string]$Body = "",
        [hashtable]$Headers = @{}
    )
    
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "测试: $Name" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "请求: $Method $Url"
    
    try {
        if ($Body) {
            Write-Host "Body: $Body"
        }
        
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
        }
        
        if ($Body) {
            $params.Body = $Body
        }
        
        if ($Headers.Count -gt 0) {
            $params.Headers = $Headers
        }
        
        $response = Invoke-RestMethod @params
        Write-Host "响应:" -ForegroundColor Green
        $response | ConvertTo-Json -Depth 5
        return $response
    }
    catch {
        Write-Host "错误: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# ========================================
# 1. 用户注册测试
# ========================================
$registerBody = @{
    username = "testuser_$(Get-Random -Minimum 1000 -Maximum 9999)"
    password = "123456"
    nickname = "测试用户"
    email = "test@example.com"
} | ConvertTo-Json -Compress

$registerResult = Test-API -Name "用户注册" -Method "POST" -Url "$BaseUrl/auth/register" -Body $registerBody

# ========================================
# 2. 用户登录测试
# ========================================
$loginBody = @{
    username = "testuser4"
    password = "123456"
} | ConvertTo-Json -Compress

$loginResult = Test-API -Name "用户登录" -Method "POST" -Url "$BaseUrl/auth/login" -Body $loginBody

# ========================================
# 3. 获取用户信息测试（需要Token）
# ========================================
if ($loginResult -and $loginResult.data.accessToken) {
    $token = $loginResult.data.accessToken
    $headers = @{ "Authorization" = "Bearer $token" }
    
    Test-API -Name "获取用户信息" -Method "GET" -Url "$BaseUrl/user/info" -Headers $headers
    
    # ========================================
    # 4. Token刷新测试
    # ========================================
    if ($loginResult.data.refreshToken) {
        $refreshBody = @{
            refreshToken = $loginResult.data.refreshToken
        } | ConvertTo-Json -Compress
        
        $refreshResult = Test-API -Name "Token刷新" -Method "POST" -Url "$BaseUrl/auth/refresh" -Body $refreshBody
        
        # 使用新Token测试
        if ($refreshResult -and $refreshResult.data.accessToken) {
            $newToken = $refreshResult.data.accessToken
            $newHeaders = @{ "Authorization" = "Bearer $newToken" }
            Test-API -Name "使用新Token获取用户信息" -Method "GET" -Url "$BaseUrl/user/info" -Headers $newHeaders
        }
    }
    
    # ========================================
    # 5. 用户登出测试
    # ========================================
    Test-API -Name "用户登出" -Method "POST" -Url "$BaseUrl/auth/logout" -Headers $headers
}

# ========================================
# 6. 无Token访问测试
# ========================================
Test-API -Name "无Token访问受保护API" -Method "GET" -Url "$BaseUrl/user/info"

# ========================================
# 7. 无效Token测试
# ========================================
Test-API -Name "无效Token访问" -Method "GET" -Url "$BaseUrl/user/info" -Headers @{ "Authorization" = "Bearer invalid_token" }

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "测试完成!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

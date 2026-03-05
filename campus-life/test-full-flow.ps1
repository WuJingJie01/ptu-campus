$body = Get-Content "d:\workspace\projects\ptu-campus\campus-life\test-login.json" -Raw
$response = Invoke-RestMethod -Uri "http://localhost:10001/auth/login" -Method Post -Body $body -ContentType "application/json"
$token = $response.data.accessToken
Write-Host "AccessToken: $token"
Write-Host ""
Write-Host "Testing protected API with token..."
$headers = @{"Authorization"="Bearer $token"}
Invoke-RestMethod -Uri "http://localhost:10001/user/info" -Method Get -Headers $headers

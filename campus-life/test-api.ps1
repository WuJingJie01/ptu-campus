$token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTc3MjE3ODAwMSwiZXhwIjoxNzcyMTc4OTAxfQ.-6g_SgqYssTUFRiRR2K38zRiml1Ze1cxHAp_-9nXUdY"
$headers = @{
    "Authorization" = "Bearer $token"
}
Write-Host "Testing protected API with AccessToken..."
Write-Host ""
Invoke-RestMethod -Uri "http://localhost:10001/user/info" -Method Get -Headers $headers

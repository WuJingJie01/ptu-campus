<template>
  <MainLayout>
    <div class="auth-page">
      <Card class="auth-card">
        <div class="auth-header">
          <div class="auth-logo">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
            </svg>
          </div>
          <h1>莆田学院校园生活系统</h1>
          <p>创建新账户</p>
        </div>

        <form class="auth-form" @submit.prevent="handleRegister">
          <div class="form-group">
            <Input
              v-model="form.email"
              type="email"
              placeholder="请输入邮箱"
              size="lg"
            />
          </div>
          <div class="form-group verify-code-group">
            <Input
              v-model="form.code"
              placeholder="验证码"
              size="lg"
            />
            <Button 
              type="secondary" 
              size="lg" 
              :disabled="isCounting || !isValidEmail"
              @click="sendCode"
              class="send-code-btn"
            >
              {{ isCounting ? `${count}s后重试` : '获取验证码' }}
            </Button>
          </div>
          <div class="form-group">
            <Input
              v-model="form.password"
              type="password"
              placeholder="请输入密码（至少6位）"
              size="lg"
            />
          </div>
          <div class="form-group">
            <Input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请确认密码"
              size="lg"
            />
          </div>
          <div class="form-options">
            <label class="agreement">
              <input type="checkbox" v-model="form.agree" />
              <span>我已阅读并同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a></span>
            </label>
          </div>
          <Button type="primary" size="lg" block :loading="loading" @click="handleRegister">
            注册
          </Button>
        </form>

        <div class="auth-footer">
          <p>已有账户？ <router-link to="/login">立即登录</router-link></p>
        </div>
      </Card>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Input, Button } from '@/components'
import { useCountDown } from '@/composables/useCountDown'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  email: '',
  code: '',
  password: '',
  confirmPassword: '',
  agree: false
})

const { count, isCounting, startCountDown } = useCountDown()

const isValidEmail = computed(() => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)
})

const sendCode = async () => {
  if (!isValidEmail.value) {
    alert('请输入有效的邮箱地址')
    return
  }
  
  startCountDown()
  alert(`验证码已发送至 ${form.email}`)
}

// 注册处理函数
const handleRegister = async () => {
  // 表单完整性校验
  if (!form.email || !form.code || !form.password || !form.confirmPassword) {
    alert('请填写完整信息')
    return
  }
  
  // 密码一致性校验
  if (form.password !== form.confirmPassword) {
    alert('两次密码输入不一致')
    return
  }
  
  // 协议勾选校验
  if (!form.agree) {
    alert('请阅读并同意用户协议')
    return
  }
  
  loading.value = true
  
  try {
    // 模拟网络请求
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    alert('注册成功，请登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.auth-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-lg;
  // background-color: $bg-secondary;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  margin-bottom: 0;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.6);
}

.auth-header {
  text-align: center;
  margin-bottom: $spacing-xl;
}

.auth-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, $primary-color, $primary-light);
  border-radius: 16px;
  color: $text-white;
  margin-bottom: $spacing-md;
  box-shadow: 0 4px 12px rgba($primary-color, 0.3);
}

.auth-header h1 {
  font-size: $font-size-xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.auth-header p {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.form-group {
  margin-bottom: 0;
}

.verify-code-group {
  display: flex;
  gap: $spacing-sm;
  
  .send-code-btn {
    flex-shrink: 0;
    width: 110px;
    padding: 0;
    font-size: $font-size-sm;
  }
}

.form-options {
  margin-bottom: $spacing-sm;
}

.agreement {
  display: flex;
  align-items: flex-start;
  gap: $spacing-xs;
  font-size: $font-size-sm;
  color: $text-secondary;
  cursor: pointer;
  
  input {
    margin-top: 3px;
    accent-color: $primary-color;
  }
  
  a {
    color: $primary-color;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.auth-footer {
  text-align: center;
  margin-top: $spacing-xl;
  padding-top: $spacing-lg;
  border-top: 1px solid rgba($border-color, 0.5);
  
  p {
    font-size: $font-size-sm;
    color: $text-muted;
    margin: 0;
  }
  
  a {
    color: $primary-color;
    font-weight: $font-weight-medium;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
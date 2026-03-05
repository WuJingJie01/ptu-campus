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
          <p>欢迎回来，同学</p>
        </div>

        <Tabs v-model="loginType" :tabs="tabs" class="auth-tabs">
          <Transition name="fade" mode="out-in">
            <div v-if="loginType === 'account'" key="account" class="tab-content">
              <form class="auth-form" @submit.prevent="handleLogin">
                <div class="form-group">
                  <Input
                    v-model="accountForm.username"
                    placeholder="请输入用户名/学号"
                    size="lg"
                  />
                </div>
                <div class="form-group">
                  <Input
                    v-model="accountForm.password"
                    type="password"
                    placeholder="请输入密码"
                    size="lg"
                  />
                </div>
                <div class="form-options">
                  <label class="remember">
                    <input type="checkbox" v-model="accountForm.remember" />
                    <span>记住我</span>
                  </label>
                  <a href="#" class="forgot">忘记密码？</a>
                </div>
                <Button type="primary" size="lg" block :loading="loading" @click="handleLogin">
                  登录
                </Button>
              </form>
            </div>

            <div v-else key="email" class="tab-content">
              <form class="auth-form" @submit.prevent="handleLogin">
                <div class="form-group">
                  <Input
                    v-model="emailForm.email"
                    type="email"
                    placeholder="请输入邮箱"
                    size="lg"
                  />
                </div>
                <div class="form-group verify-code-group">
                  <Input
                    v-model="emailForm.code"
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
                <div class="form-options">
                  <label class="remember">
                    <input type="checkbox" v-model="emailForm.remember" />
                    <span>记住我</span>
                  </label>
                </div>
                <Button type="primary" size="lg" block :loading="loading" @click="handleLogin">
                  登录
                </Button>
              </form>
            </div>
          </Transition>
        </Tabs>

        <div class="auth-footer">
          <p>还没有账户？ <router-link to="/register">立即注册</router-link></p>
        </div>
      </Card>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Input, Button, Tabs } from '@/components'
import { useUserStore } from '@/stores/user'
import { useCountDown } from '@/composables/useCountDown'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const loginType = ref('account')
const tabs = [
  { label: '账号登录', value: 'account' },
  { label: '邮箱登录', value: 'email' }
]

// Account Login
const accountForm = reactive({
  username: '',
  password: '',
  remember: false
})

// Email Login
const emailForm = reactive({
  email: '',
  code: '',
  remember: false
})

const { count, isCounting, startCountDown } = useCountDown()

const isValidEmail = computed(() => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailForm.email)
})

const sendCode = async () => {
  if (!isValidEmail.value) {
    alert('请输入有效的邮箱地址')
    return
  }
  
  // Simulate API call
  startCountDown()
  alert(`验证码已发送至 ${emailForm.email}`)
}

// 登录处理函数
const handleLogin = async () => {
  loading.value = true
  
  try {
    // 模拟网络请求延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 根据登录类型进行表单校验
    if (loginType.value === 'account') {
       if (!accountForm.username || !accountForm.password) {
        alert('请填写用户名和密码')
        loading.value = false
        return
      }
    } else {
       if (!emailForm.email || !emailForm.code) {
        alert('请填写邮箱和验证码')
        loading.value = false
        return
      }
    }

    // 设置用户信息（模拟数据）
    userStore.setUser({
      id: 1,
      username: loginType.value === 'account' ? accountForm.username : emailForm.email.split('@')[0],
      nickname: loginType.value === 'account' ? accountForm.username : '同学',
      avatar: '',
      email: loginType.value === 'email' ? emailForm.email : '',
      isVerified: false,
      fansCount: 0,
      followCount: 0
    })
    userStore.setToken('mock-token')
    
    // 登录成功跳转首页
    router.push('/')
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
  margin-bottom: $spacing-lg;
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

.auth-tabs {
  margin-bottom: $spacing-lg;
  
  :deep(.tabs-list) {
    justify-content: center;
    border-bottom: none;
    gap: $spacing-xl;
    margin-bottom: $spacing-lg;
  }
  
  :deep(.tabs-tab) {
    padding: $spacing-xs 0;
    font-size: $font-size-base;
    color: $text-muted;
  }
  
  :deep(.tabs-tab-active) {
    color: $primary-color;
    font-weight: $font-weight-bold;
  }
  
  :deep(.tabs-content) {
    padding: 0;
  }
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
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-sm;
  color: $text-secondary;
  cursor: pointer;
  
  input {
    accent-color: $primary-color;
  }
}

.forgot {
  font-size: $font-size-sm;
  color: $primary-color;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
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

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
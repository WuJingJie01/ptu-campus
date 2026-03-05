<template>
  <MainLayout>
    <div class="settings-page">
      <div class="settings-container">
        <aside class="settings-sidebar">
          <nav class="settings-nav">
            <button
              v-for="item in navItems"
              :key="item.value"
              :class="['nav-item', { 'nav-item-active': activeNav === item.value }]"
              @click="activeNav = item.value"
            >
              {{ item.label }}
            </button>
          </nav>
        </aside>

        <main class="settings-main">
          <Card v-if="activeNav === 'profile'" title="个人资料" class="settings-card">
            <form class="settings-form">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">头像</label>
                  <div class="avatar-upload">
                    <Avatar :src="user.avatar" :name="user.nickname" size="xl" />
                    <Button type="outline" size="sm">更换头像</Button>
                  </div>
                </div>
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">昵称</label>
                  <Input v-model="user.nickname" placeholder="请输入昵称" />
                </div>
                <div class="form-group">
                  <label class="form-label">用户名</label>
                  <Input v-model="user.username" placeholder="请输入用户名" disabled />
                </div>
              </div>
              <div class="form-group">
                <label class="form-label">个人简介</label>
                <textarea v-model="user.bio" class="form-textarea" placeholder="介绍一下自己..." rows="3"></textarea>
              </div>
              <div class="form-actions">
                <Button type="primary" @click="saveProfile">保存修改</Button>
              </div>
            </form>
          </Card>

          <Card v-else-if="activeNav === 'verify'" title="校园认证" class="settings-card">
            <div class="verify-status" v-if="user.isVerified">
              <div class="verify-icon success">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                  <polyline points="22 4 12 14.01 9 11.01" />
                </svg>
              </div>
              <h3>已认证</h3>
              <p>您的校园身份已认证通过</p>
            </div>
            <form v-else class="settings-form">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">学院</label>
                  <Input v-model="verifyForm.college" placeholder="请输入所在学院" />
                </div>
                <div class="form-group">
                  <label class="form-label">年级</label>
                  <Input v-model="verifyForm.grade" placeholder="请输入年级" />
                </div>
              </div>
              <div class="form-group">
                <label class="form-label">学号</label>
                <Input v-model="verifyForm.studentId" placeholder="请输入学号" />
              </div>
              <div class="form-actions">
                <Button type="primary" @click="submitVerify">提交认证</Button>
              </div>
            </form>
          </Card>

          <Card v-else-if="activeNav === 'security'" title="账号安全" class="settings-card">
            <div class="security-list">
              <div class="security-item">
                <div class="security-info">
                  <h4>登录密码</h4>
                  <p>定期更换密码可以保护账号安全</p>
                </div>
                <Button type="outline" size="sm">修改密码</Button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h4>绑定邮箱</h4>
                  <p>{{ user.email || '未绑定' }}</p>
                </div>
                <Button type="outline" size="sm">{{ user.email ? '更换邮箱' : '绑定邮箱' }}</Button>
              </div>
            </div>
          </Card>

          <Card v-else-if="activeNav === 'notification'" title="通知设置" class="settings-card">
            <div class="notification-list">
              <div class="notification-item">
                <div class="notification-info">
                  <h4>系统通知</h4>
                  <p>接收系统重要通知</p>
                </div>
                <label class="toggle">
                  <input type="checkbox" v-model="notifications.system" />
                  <span class="toggle-slider"></span>
                </label>
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <h4>关注动态</h4>
                  <p>关注用户发布新内容时通知</p>
                </div>
                <label class="toggle">
                  <input type="checkbox" v-model="notifications.follow" />
                  <span class="toggle-slider"></span>
                </label>
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <h4>邮件推送</h4>
                  <p>重要消息通过邮件推送</p>
                </div>
                <label class="toggle">
                  <input type="checkbox" v-model="notifications.email" />
                  <span class="toggle-slider"></span>
                </label>
              </div>
            </div>
          </Card>
        </main>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Button, Input, Avatar } from '@/components'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const activeNav = ref('profile')

const navItems = [
  { label: '个人资料', value: 'profile' },
  { label: '校园认证', value: 'verify' },
  { label: '账号安全', value: 'security' },
  { label: '通知设置', value: 'notification' }
]

const user = reactive({
  avatar: '',
  nickname: '张同学',
  username: 'zhangsan',
  bio: '热爱学习，热爱生活',
  email: 'zhangsan@example.com',
  isVerified: true
})

const verifyForm = reactive({
  college: '',
  grade: '',
  studentId: ''
})

const notifications = reactive({
  system: true,
  follow: true,
  email: false
})

const saveProfile = () => {
  alert('保存成功')
}

const submitVerify = () => {
  alert('认证申请已提交，等待审核')
}

onMounted(() => {
  if (!userStore.isAuth) {
    router.push('/login')
  }
})
</script>

<style lang="scss" scoped>
.settings-page {
  min-height: 100vh;
  background: $bg-secondary;
}

.settings-container {
  max-width: 1000px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: $spacing-xl;
  padding: $spacing-lg;
}

.settings-sidebar {
  background: $bg-primary;
  border-radius: $radius-lg;
  padding: $spacing-md;
  height: fit-content;
}

.settings-nav {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.nav-item {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: transparent;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  text-align: left;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
  
  &-active {
    color: $primary-color;
    background: $primary-bg;
  }
}

.settings-main {
  min-width: 0;
}

.settings-card {
  margin-bottom: 0;
}

.settings-form {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.form-label {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-secondary;
}

.form-textarea {
  width: 100%;
  padding: $spacing-md;
  font-size: $font-size-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-lg;
  resize: vertical;
  outline: none;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: $spacing-lg;
}

.form-actions {
  padding-top: $spacing-md;
}

.verify-status {
  text-align: center;
  padding: $spacing-xl;
}

.verify-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: $spacing-lg;
  
  &.success {
    background: rgba($success-color, 0.1);
    color: $success-color;
  }
}

.verify-status h3 {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-sm;
}

.verify-status p {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0;
}

.security-list,
.notification-list {
  display: flex;
  flex-direction: column;
}

.security-item,
.notification-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-lg 0;
  border-bottom: 1px solid $border-light;
  
  &:last-child {
    border-bottom: none;
  }
}

.security-info h4,
.notification-info h4 {
  font-size: $font-size-base;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.security-info p,
.notification-info p {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0;
}

.toggle {
  position: relative;
  display: inline-block;
  width: 44px;
  height: 24px;
  
  input {
    opacity: 0;
    width: 0;
    height: 0;
    
    &:checked + .toggle-slider {
      background: $primary-color;
      
      &::before {
        transform: translateX(20px);
      }
    }
  }
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  inset: 0;
  background: $border-dark;
  border-radius: $radius-full;
  transition: all $transition-fast;
  
  &::before {
    content: '';
    position: absolute;
    height: 18px;
    width: 18px;
    left: 3px;
    bottom: 3px;
    background: $text-white;
    border-radius: 50%;
    transition: all $transition-fast;
  }
}
</style>

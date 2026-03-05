<template>
  <MainLayout>
    <div class="post-page">
      <div class="post-container">
        <Card class="post-card">
          <template #header>
            <h2 class="post-title">发布兼职</h2>
          </template>

          <form class="post-form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label class="form-label">兼职标题 <span class="required">*</span></label>
              <Input
                v-model="form.title"
                placeholder="请输入兼职标题（5-30字）"
                :maxlength="30"
              />
              <p class="form-hint">{{ form.title.length }}/30</p>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">薪资待遇 <span class="required">*</span></label>
                <Input v-model="form.salary" placeholder="如：15元/小时" />
              </div>
              <div class="form-group">
                <label class="form-label">结算方式 <span class="required">*</span></label>
                <div class="select-wrapper">
                  <select v-model="form.paymentMethod" class="form-select">
                    <option value="">请选择</option>
                    <option value="cash">现金</option>
                    <option value="wechat">微信</option>
                    <option value="alipay">支付宝</option>
                    <option value="bank">银行转账</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">结算时间 <span class="required">*</span></label>
              <div class="select-wrapper">
                <select v-model="form.paymentTime" class="form-select">
                  <option value="">请选择</option>
                  <option value="daily">日结</option>
                  <option value="weekly">周结</option>
                  <option value="monthly">月结</option>
                  <option value="finish">完工结算</option>
                </select>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">工作地点 <span class="required">*</span></label>
                <Input v-model="form.location" placeholder="请输入工作地点" />
              </div>
              <div class="form-group">
                <label class="form-label">工作时间 <span class="required">*</span></label>
                <Input v-model="form.workTime" placeholder="如：周一至周五 18:00-21:00" />
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">工作内容 <span class="required">*</span></label>
              <textarea
                v-model="form.description"
                class="form-textarea"
                placeholder="请详细描述工作内容..."
                rows="5"
                :maxlength="500"
              ></textarea>
              <p class="form-hint">{{ form.description.length }}/500</p>
            </div>

            <div class="form-group">
              <label class="form-label">任职要求 <span class="required">*</span></label>
              <textarea
                v-model="form.requirements"
                class="form-textarea"
                placeholder="请描述任职要求，每行一条..."
                rows="4"
                :maxlength="300"
              ></textarea>
              <p class="form-hint">{{ form.requirements.length }}/300</p>
            </div>

            <div class="form-group">
              <label class="form-checkbox">
                <input type="checkbox" v-model="form.urgent" />
                <span>标记为急招</span>
              </label>
            </div>

            <div class="form-actions">
              <Button type="ghost" @click="handleSave">保存草稿</Button>
              <Button type="primary" :loading="submitting" @click="handleSubmit">
                发布兼职
              </Button>
            </div>
          </form>
        </Card>

        <aside class="post-sidebar">
          <Card title="发布须知" class="sidebar-card">
            <ul class="notice-list">
              <li>请确保兼职信息真实有效</li>
              <li>禁止发布虚假招聘信息</li>
              <li>禁止发布需要交押金的兼职</li>
              <li>薪资待遇需如实填写</li>
              <li>发布后内容将进入审核状态</li>
            </ul>
          </Card>
        </aside>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Button, Input } from '@/components'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const submitting = ref(false)

const form = reactive({
  title: '',
  salary: '',
  paymentMethod: '',
  paymentTime: '',
  location: '',
  workTime: '',
  description: '',
  requirements: '',
  urgent: false
})

const handleSave = () => {
  alert('草稿已保存')
}

const handleSubmit = () => {
  if (!form.title || !form.salary || !form.paymentMethod || !form.paymentTime || !form.location || !form.workTime || !form.description || !form.requirements) {
    alert('请填写完整信息')
    return
  }
  
  submitting.value = true
  setTimeout(() => {
    submitting.value = false
    alert('发布成功，等待审核')
    router.push('/content')
  }, 1000)
}

onMounted(() => {
  if (!userStore.isAuth) {
    router.push('/login')
  }
})
</script>

<style lang="scss" scoped>
.post-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.post-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 300px;
  }
}

.post-card {
  margin-bottom: 0;
}

.post-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0;
}

.post-form {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
}

.form-label {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-secondary;
}

.required {
  color: $danger-color;
}

.form-hint {
  font-size: $font-size-xs;
  color: $text-muted;
  margin: 0;
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
  transition: all $transition-fast;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
}

.select-wrapper {
  position: relative;
}

.form-select {
  width: 100%;
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-md;
  outline: none;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
}

.form-checkbox {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $text-secondary;
  cursor: pointer;
  
  input {
    width: 16px;
    height: 16px;
    accent-color: $primary-color;
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-md;
  padding-top: $spacing-lg;
  border-top: 1px solid $border-color;
}

.post-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.notice-list {
  padding-left: $spacing-lg;
  margin: 0;
  
  li {
    font-size: $font-size-sm;
    color: $text-secondary;
    line-height: $line-height-relaxed;
    margin-bottom: $spacing-sm;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>

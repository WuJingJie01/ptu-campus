<template>
  <MainLayout>
    <div class="post-page">
      <div class="post-container">
        <Card class="post-card">
          <template #header>
            <h2 class="post-title">发布商品</h2>
          </template>

          <form class="post-form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label class="form-label">商品图片 <span class="required">*</span></label>
              <div class="image-upload">
                <div class="image-list">
                  <div v-for="(image, index) in form.images" :key="index" class="image-item">
                    <img :src="image" :alt="`图片${index + 1}`" />
                    <button type="button" class="image-remove" @click="removeImage(index)">×</button>
                  </div>
                  <label v-if="form.images.length < 5" class="image-add">
                    <input type="file" accept="image/*" hidden @change="handleImageUpload" />
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                      <circle cx="8.5" cy="8.5" r="1.5" />
                      <polyline points="21 15 16 10 5 21" />
                    </svg>
                    <span>添加图片</span>
                  </label>
                </div>
                <p class="form-hint">最多上传5张图片，第一张为封面图</p>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">商品标题 <span class="required">*</span></label>
              <Input
                v-model="form.title"
                placeholder="请输入商品标题（5-30字）"
                :maxlength="30"
              />
              <p class="form-hint">{{ form.title.length }}/30</p>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">商品价格 <span class="required">*</span></label>
                <Input
                  v-model="form.price"
                  type="number"
                  placeholder="请输入价格"
                >
                  <template #prefix>¥</template>
                </Input>
              </div>
              <div class="form-group">
                <label class="form-label">原价（选填）</label>
                <Input
                  v-model="form.originalPrice"
                  type="number"
                  placeholder="请输入原价"
                >
                  <template #prefix>¥</template>
                </Input>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">商品分类 <span class="required">*</span></label>
              <div class="category-options">
                <button
                  v-for="category in categories"
                  :key="category.value"
                  type="button"
                  :class="['category-option', { 'category-option-active': form.category === category.value }]"
                  @click="form.category = category.value"
                >
                  {{ category.label }}
                </button>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">商品描述 <span class="required">*</span></label>
              <textarea
                v-model="form.description"
                class="form-textarea"
                placeholder="请详细描述商品信息，包括新旧程度、购买时间、使用情况等..."
                rows="6"
                :maxlength="500"
              ></textarea>
              <p class="form-hint">{{ form.description.length }}/500</p>
            </div>

            <div class="form-group">
              <label class="form-label">交易方式 <span class="required">*</span></label>
              <div class="trade-options">
                <button
                  v-for="method in tradeMethods"
                  :key="method.value"
                  type="button"
                  :class="['trade-option', { 'trade-option-active': form.tradeMethod === method.value }]"
                  @click="form.tradeMethod = method.value"
                >
                  {{ method.label }}
                </button>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">校园信息（选填）</label>
              <div class="form-row">
                <Input v-model="form.college" placeholder="所在学院" />
                <Input v-model="form.grade" placeholder="年级" />
              </div>
            </div>

            <div class="form-actions">
              <Button type="ghost" @click="handleSave">保存草稿</Button>
              <Button type="primary" :loading="submitting" @click="handleSubmit">
                发布商品
              </Button>
            </div>
          </form>
        </Card>

        <aside class="post-sidebar">
          <Card title="发布须知" class="sidebar-card">
            <ul class="notice-list">
              <li>请确保商品信息真实有效</li>
              <li>禁止发布违禁品、假冒伪劣商品</li>
              <li>禁止发布虚假价格信息</li>
              <li>建议当面交易，注意安全</li>
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
  images: [] as string[],
  title: '',
  price: '',
  originalPrice: '',
  category: '',
  description: '',
  tradeMethod: '',
  college: '',
  grade: ''
})

const categories = [
  { label: '数码电子', value: 'digital' },
  { label: '书籍教材', value: 'books' },
  { label: '生活用品', value: 'daily' },
  { label: '服装鞋帽', value: 'clothes' },
  { label: '运动户外', value: 'sports' },
  { label: '其他', value: 'other' }
]

const tradeMethods = [
  { label: '校内当面交易', value: 'face' },
  { label: '自提', value: 'self' }
]

const handleImageUpload = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file && form.images.length < 5) {
    form.images.push(URL.createObjectURL(file))
  }
}

const removeImage = (index: number) => {
  form.images.splice(index, 1)
}

const handleSave = () => {
  alert('草稿已保存')
}

const handleSubmit = () => {
  if (!form.images.length || !form.title || !form.price || !form.category || !form.description || !form.tradeMethod) {
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

.image-upload {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-md;
}

.image-item {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: $radius-lg;
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.image-remove {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: $text-white;
  background: rgba(0, 0, 0, 0.5);
  border: none;
  border-radius: 50%;
  cursor: pointer;
}

.image-add {
  width: 100px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: $spacing-xs;
  background: $bg-tertiary;
  border: 2px dashed $border-color;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    border-color: $primary-color;
    color: $primary-color;
  }
  
  span {
    font-size: $font-size-xs;
    color: $text-muted;
  }
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

.category-options,
.trade-options {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
}

.category-option,
.trade-option {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    border-color: $primary-color;
  }
  
  &-active {
    color: $text-white;
    background: $primary-color;
    border-color: $primary-color;
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

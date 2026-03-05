<template>
  <MainLayout>
    <div class="post-page">
      <div class="post-container">
        <Card class="post-card">
          <template #header>
            <h2 class="post-title">发布帖子</h2>
          </template>

          <form class="post-form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label class="form-label">帖子标题 <span class="required">*</span></label>
              <Input
                v-model="form.title"
                placeholder="请输入帖子标题（5-50字）"
                :maxlength="50"
              />
              <p class="form-hint">{{ form.title.length }}/50</p>
            </div>

            <div class="form-group">
              <label class="form-label">帖子分类 <span class="required">*</span></label>
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
              <label class="form-label">帖子内容 <span class="required">*</span></label>
              <textarea
                v-model="form.content"
                class="form-textarea"
                placeholder="请输入帖子内容，分享你的想法..."
                rows="10"
                :maxlength="2000"
              ></textarea>
              <p class="form-hint">{{ form.content.length }}/2000</p>
            </div>

            <div class="form-group">
              <label class="form-label">上传图片</label>
              <div class="image-upload">
                <div class="image-list">
                  <div v-for="(img, index) in form.images" :key="index" class="image-item">
                    <img :src="img" alt="uploaded" />
                    <button type="button" class="image-remove" @click="removeImage(index)">×</button>
                  </div>
                  <div v-if="form.images.length < 9" class="image-uploader" @click="triggerUpload">
                    <input
                      ref="fileInput"
                      type="file"
                      accept="image/*"
                      multiple
                      class="file-input"
                      @change="handleFileChange"
                    />
                    <div class="uploader-icon">+</div>
                    <span class="uploader-text">上传图片</span>
                  </div>
                </div>
                <p class="form-hint">最多上传9张图片，支持jpg/png格式，单张不超过5MB</p>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">标签（选填）</label>
              <div class="tags-input">
                <div class="tags-list">
                  <span v-for="(tag, index) in form.tags" :key="index" class="tag-item">
                    {{ tag }}
                    <button type="button" class="tag-remove" @click="removeTag(index)">×</button>
                  </span>
                </div>
                <input
                  v-if="form.tags.length < 5"
                  v-model="tagInput"
                  type="text"
                  class="tag-input"
                  placeholder="输入标签后按回车添加"
                  @keydown.enter.prevent="addTag"
                />
              </div>
              <p class="form-hint">最多添加5个标签，每个标签不超过10个字</p>
            </div>

            <div class="form-actions">
              <Button type="ghost" @click="handleSave">保存草稿</Button>
              <Button type="primary" :loading="submitting" @click="handleSubmit">
                发布帖子
              </Button>
            </div>
          </form>
        </Card>

        <aside class="post-sidebar">
          <Card title="发布须知" class="sidebar-card">
            <ul class="notice-list">
              <li>请确保发布内容真实、合法</li>
              <li>禁止发布广告、色情、暴力等违规内容</li>
              <li>禁止发布虚假信息、谣言</li>
              <li>尊重他人，文明发言</li>
              <li>发布后内容将进入审核状态</li>
            </ul>
          </Card>

          <Card title="发帖技巧" class="sidebar-card">
            <ul class="tips-list">
              <li>标题简洁明了，突出主题</li>
              <li>内容条理清晰，分段表述</li>
              <li>添加合适的标签，增加曝光</li>
              <li>选择正确的分类，方便查找</li>
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
const tagInput = ref('')

const form = reactive({
  title: '',
  category: '',
  content: '',
  tags: [] as string[],
  images: [] as string[]
})

const fileInput = ref<HTMLInputElement | null>(null)

const triggerUpload = () => {
  fileInput.value?.click()
}

const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (input.files) {
    const files = Array.from(input.files)
    files.forEach(file => {
      if (form.images.length >= 9) return
      
      const reader = new FileReader()
      reader.onload = (e) => {
        if (e.target?.result) {
          form.images.push(e.target.result as string)
        }
      }
      reader.readAsDataURL(file)
    })
  }
}

const removeImage = (index: number) => {
  form.images.splice(index, 1)
}

const categories = [
  { label: '校园生活', value: 'life' },
  { label: '学习交流', value: 'study' },
  { label: '失物招领', value: 'lost' },
  { label: '二手交易', value: 'trade' },
  { label: '求职招聘', value: 'job' },
  { label: '其他', value: 'other' }
]

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && tag.length <= 10 && !form.tags.includes(tag) && form.tags.length < 5) {
    form.tags.push(tag)
    tagInput.value = ''
  }
}

const removeTag = (index: number) => {
  form.tags.splice(index, 1)
}

const handleSave = () => {
  alert('草稿已保存')
}

const handleSubmit = () => {
  if (!form.title || !form.category || !form.content) {
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
  
  &::placeholder {
    color: $text-light;
  }
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
  border-radius: $radius-md;
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
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  
  &:hover {
    background: rgba(0, 0, 0, 0.7);
  }
}

.image-uploader {
  width: 100px;
  height: 100px;
  border: 2px dashed $border-color;
  border-radius: $radius-md;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    border-color: $primary-color;
    color: $primary-color;
  }
}

.file-input {
  display: none;
}

.uploader-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.uploader-text {
  font-size: $font-size-xs;
  color: $text-muted;
}

.category-options {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
}

.category-option {
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
    
    &:hover {
      color: $text-white;
      background: $primary-dark;
    }
  }
}

.tags-input {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-lg;
  
  &:focus-within {
    background: $bg-primary;
    border-color: $primary-color;
  }
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: $spacing-xs $spacing-sm;
  font-size: $font-size-xs;
  color: $primary-color;
  background: $primary-bg;
  border-radius: $radius-full;
}

.tag-remove {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 14px;
  height: 14px;
  font-size: 12px;
  color: $primary-color;
  background: transparent;
  border: none;
  cursor: pointer;
  
  &:hover {
    color: $danger-color;
  }
}

.tag-input {
  flex: 1;
  min-width: 150px;
  padding: $spacing-xs;
  font-size: $font-size-sm;
  background: transparent;
  border: none;
  outline: none;
  
  &::placeholder {
    color: $text-light;
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

.notice-list,
.tips-list {
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

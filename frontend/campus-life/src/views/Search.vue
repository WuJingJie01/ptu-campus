<template>
  <MainLayout>
    <div class="search-page">
      <div class="search-container">
        <div class="search-header">
          <h1>搜索结果</h1>
          <p>关键词: "{{ keyword }}"</p>
        </div>

        <Tabs :tabs="searchTabs" v-model="activeTab">
          <div v-if="activeTab === 'posts'" class="search-results">
            <div v-if="posts.length" class="results-list">
              <div v-for="post in posts" :key="post.id" class="result-item" @click="router.push(`/forum/${post.id}`)">
                <h4 v-html="highlightKeyword(post.title)"></h4>
                <p>{{ post.content }}</p>
                <div class="result-meta">
                  <span>{{ post.author }}</span>
                  <span>{{ post.time }}</span>
                  <span>{{ post.likes }} 赞</span>
                </div>
              </div>
            </div>
            <Empty v-else title="没有找到相关帖子" />
          </div>

          <div v-else-if="activeTab === 'products'" class="search-results">
            <div v-if="products.length" class="results-grid">
              <div v-for="product in products" :key="product.id" class="product-item" @click="router.push(`/market/${product.id}`)">
                <img :src="product.image" :alt="product.title" />
                <div class="product-info">
                  <h4 v-html="highlightKeyword(product.title)"></h4>
                  <span class="product-price">¥{{ product.price }}</span>
                </div>
              </div>
            </div>
            <Empty v-else title="没有找到相关商品" />
          </div>

          <div v-else-if="activeTab === 'jobs'" class="search-results">
            <div v-if="jobs.length" class="results-list">
              <div v-for="job in jobs" :key="job.id" class="result-item" @click="router.push(`/job/${job.id}`)">
                <h4 v-html="highlightKeyword(job.title)"></h4>
                <div class="result-meta">
                  <span class="job-salary">{{ job.salary }}</span>
                  <span>{{ job.location }}</span>
                  <span>{{ job.time }}</span>
                </div>
              </div>
            </div>
            <Empty v-else title="没有找到相关兼职" />
          </div>

          <div v-else-if="activeTab === 'users'" class="search-results">
            <div v-if="users.length" class="users-list">
              <div v-for="user in users" :key="user.id" class="user-item" @click="router.push(`/user/${user.id}`)">
                <Avatar :src="user.avatar" :name="user.name" size="lg" />
                <div class="user-info">
                  <h4 v-html="highlightKeyword(user.name)"></h4>
                  <p v-if="user.college">{{ user.college }} · {{ user.grade }}</p>
                </div>
                <Button type="primary" size="sm">关注</Button>
              </div>
            </div>
            <Empty v-else title="没有找到相关用户" />
          </div>
        </Tabs>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Tabs, Avatar, Button, Empty } from '@/components'

const router = useRouter()
const route = useRoute()

const keyword = computed(() => (route.query.q as string) || '')
const activeTab = ref('posts')

const searchTabs = [
  { label: '帖子', value: 'posts' },
  { label: '商品', value: 'products' },
  { label: '兼职', value: 'jobs' },
  { label: '用户', value: 'users' }
]

const posts = ref([
  { id: 1, title: '关于图书馆自习室预约系统的使用心得分享', content: '最近发现图书馆的预约系统真的很好用...', author: '张同学', time: '10分钟前', likes: 128 },
  { id: 2, title: '图书馆开放时间调整通知', content: '根据学校安排，图书馆开放时间调整为...', author: '图书馆', time: '1天前', likes: 56 }
])

const products = ref([
  { id: 1, title: '图书馆借阅卡', price: 10, image: 'https://picsum.photos/200/200?random=70' }
])

const jobs = ref([
  { id: 1, title: '图书馆助理管理员', salary: '15元/小时', location: '图书馆', time: '今天' }
])

const users = ref([
  { id: 1, name: '图书馆小助手', avatar: '', college: '图书馆', grade: '' }
])

const highlightKeyword = (text: string) => {
  if (!keyword.value) return text
  const regex = new RegExp(`(${keyword.value})`, 'gi')
  return text.replace(regex, '<mark>$1</mark>')
}
</script>

<style lang="scss" scoped>
.search-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.search-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
}

.search-header {
  margin-bottom: $spacing-xl;
  
  h1 {
    font-size: $font-size-2xl;
    font-weight: $font-weight-bold;
    color: $text-primary;
    margin: 0 0 $spacing-sm;
  }
  
  p {
    font-size: $font-size-base;
    color: $text-muted;
    margin: 0;
  }
}

.search-results {
  min-height: 300px;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.result-item {
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
  
  h4 {
    font-size: $font-size-base;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
    
    :deep(mark) {
      background: rgba($primary-color, 0.2);
      color: $primary-color;
      padding: 0 2px;
      border-radius: 2px;
    }
  }
  
  p {
    font-size: $font-size-sm;
    color: $text-secondary;
    margin: 0 0 $spacing-sm;
    
    @include truncate(2);
  }
}

.result-meta {
  display: flex;
  gap: $spacing-md;
  font-size: $font-size-xs;
  color: $text-muted;
}

.job-salary {
  color: $danger-color;
  font-weight: $font-weight-semibold;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.product-item {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
  
  img {
    width: 100%;
    aspect-ratio: 1;
    object-fit: cover;
  }
}

.product-info {
  padding: $spacing-md;
  
  h4 {
    font-size: $font-size-sm;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
    
    :deep(mark) {
      background: rgba($primary-color, 0.2);
      color: $primary-color;
      padding: 0 2px;
      border-radius: 2px;
    }
  }
}

.product-price {
  font-size: $font-size-base;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.users-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.user-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.user-info {
  flex: 1;
  
  h4 {
    font-size: $font-size-base;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
    
    :deep(mark) {
      background: rgba($primary-color, 0.2);
      color: $primary-color;
      padding: 0 2px;
      border-radius: 2px;
    }
  }
  
  p {
    font-size: $font-size-sm;
    color: $text-muted;
    margin: 0;
  }
}
</style>

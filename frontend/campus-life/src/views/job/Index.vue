<template>
  <MainLayout>
    <div class="job-page">
      <div class="job-container">
        <div class="job-main">
          <div class="job-header">
            <div class="job-filters">
              <div class="filter-group">
                <label>类型：</label>
                <div class="filter-options">
                  <button
                    v-for="type in jobTypes"
                    :key="type.value"
                    :class="['filter-btn', { 'filter-btn-active': activeType === type.value }]"
                    @click="activeType = type.value"
                  >
                    {{ type.label }}
                  </button>
                </div>
              </div>
              <div class="filter-group">
                <label>结算：</label>
                <div class="filter-options">
                  <button
                    v-for="method in paymentMethods"
                    :key="method.value"
                    :class="['filter-btn', { 'filter-btn-active': activePayment === method.value }]"
                    @click="activePayment = method.value"
                  >
                    {{ method.label }}
                  </button>
                </div>
              </div>
            </div>
            <Button type="primary" @click="router.push('/job/post')">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              发布兼职
            </Button>
          </div>

          <div class="job-list">
            <div
              v-for="job in jobs"
              :key="job.id"
              class="job-card"
              @click="router.push(`/job/${job.id}`)"
            >
              <div class="job-main-info">
                <div class="job-header-row">
                  <h3 class="job-title">{{ job.title }}</h3>
                  <Badge :type="job.urgent ? 'danger' : 'success'">
                    {{ job.urgent ? '急招' : '招聘中' }}
                  </Badge>
                </div>
                <div class="job-salary-row">
                  <span class="job-salary">{{ job.salary }}</span>
                  <span class="job-settlement">{{ job.settlement }}</span>
                </div>
                <div class="job-meta-row">
                  <span class="job-location">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                      <circle cx="12" cy="10" r="3" />
                    </svg>
                    {{ job.location }}
                  </span>
                  <span class="job-time">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="10" />
                      <polyline points="12 6 12 12 16 14" />
                    </svg>
                    {{ job.workTime }}
                  </span>
                </div>
              </div>
              <div class="job-footer">
                <div class="job-publisher">
                  <Avatar :src="job.publisher.avatar" :name="job.publisher.name" size="sm" />
                  <span>{{ job.publisher.name }}</span>
                  <span class="job-publish-time">{{ job.publishTime }}</span>
                </div>
                <div class="job-actions">
                  <button class="job-action-btn" @click.stop="handleCollect(job)">
                    <svg width="16" height="16" viewBox="0 0 24 24" :fill="job.collected ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                      <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                    </svg>
                  </button>
                  <button class="job-action-btn" @click.stop="handleShare(job)">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="18" cy="5" r="3" />
                      <circle cx="6" cy="12" r="3" />
                      <circle cx="18" cy="19" r="3" />
                      <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" />
                      <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="job-pagination">
            <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--">
              上一页
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">
              下一页
            </button>
          </div>
        </div>

        <aside class="job-sidebar">
          <Card title="🔥 热门兼职" class="sidebar-card">
            <div class="hot-list">
              <div
                v-for="item in hotJobs"
                :key="item.id"
                class="hot-item"
                @click="router.push(`/job/${item.id}`)"
              >
                <div class="hot-info">
                  <h4 class="hot-title">{{ item.title }}</h4>
                  <div class="hot-salary">{{ item.salary }}</div>
                </div>
              </div>
            </div>
          </Card>

          <Card title="💡 求职提示" class="sidebar-card">
            <ul class="tips-list">
              <li>注意保护个人信息安全</li>
              <li>警惕需要交押金的兼职</li>
              <li>确认工作时间和地点</li>
              <li>保留好工作凭证</li>
            </ul>
          </Card>
        </aside>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Button, Avatar, Badge, Card } from '@/components'

const router = useRouter()
const activeType = ref('all')
const activePayment = ref('all')
const currentPage = ref(1)
const totalPages = ref(5)

const jobTypes = [
  { label: '全部', value: 'all' },
  { label: '校内兼职', value: 'campus' },
  { label: '家教辅导', value: 'tutor' },
  { label: '实习岗位', value: 'intern' },
  { label: '临时工作', value: 'temp' }
]

const paymentMethods = [
  { label: '全部', value: 'all' },
  { label: '日结', value: 'daily' },
  { label: '周结', value: 'weekly' },
  { label: '月结', value: 'monthly' },
  { label: '完工结', value: 'finish' }
]

const jobs = ref([
  {
    id: 1,
    title: '图书馆助理管理员',
    salary: '15元/小时',
    settlement: '月结',
    location: '图书馆',
    workTime: '周一至周五 18:00-21:00',
    urgent: true,
    publisher: { name: '图书馆', avatar: '' },
    publishTime: '今天',
    collected: false
  },
  {
    id: 2,
    title: '食堂窗口兼职',
    salary: '12元/小时',
    settlement: '周结',
    location: '第一食堂',
    workTime: '周一至周五 11:00-13:00',
    urgent: false,
    publisher: { name: '食堂管理', avatar: '' },
    publishTime: '昨天',
    collected: false
  },
  {
    id: 3,
    title: '家教辅导（数学）',
    salary: '50元/小时',
    settlement: '周结',
    location: '校内/线上',
    workTime: '周末 14:00-16:00',
    urgent: false,
    publisher: { name: '张同学', avatar: '' },
    publishTime: '昨天',
    collected: true
  },
  {
    id: 4,
    title: '快递代取服务',
    salary: '3元/件',
    settlement: '日结',
    location: '校内快递点',
    workTime: '时间灵活',
    urgent: false,
    publisher: { name: '李同学', avatar: '' },
    publishTime: '2天前',
    collected: false
  },
  {
    id: 5,
    title: '活动志愿者',
    salary: '50元/天',
    settlement: '完工结',
    location: '体育馆',
    workTime: '周六 8:00-17:00',
    urgent: true,
    publisher: { name: '学生会', avatar: '' },
    publishTime: '2天前',
    collected: false
  }
])

const hotJobs = [
  { id: 1, title: '图书馆助理管理员', salary: '15元/小时' },
  { id: 2, title: '家教辅导（数学）', salary: '50元/小时' },
  { id: 3, title: '活动志愿者', salary: '50元/天' }
]

const handleCollect = (job: any) => {
  job.collected = !job.collected
}

const handleShare = (job: any) => {
  navigator.clipboard.writeText(`${window.location.origin}/job/${job.id}`)
  alert('链接已复制到剪贴板')
}
</script>

<style lang="scss" scoped>
.job-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.job-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 300px;
  }
}

.job-header {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  
  @include respond-to(md) {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
  }
}

.job-filters {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.filter-group {
  display: flex;
  align-items: flex-start;
  gap: $spacing-sm;
  
  label {
    font-size: $font-size-sm;
    color: $text-muted;
    padding-top: $spacing-sm;
    white-space: nowrap;
  }
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
}

.filter-btn {
  padding: $spacing-xs $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-tertiary;
  border: none;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
  
  &-active {
    color: $text-white;
    background: $primary-color;
  }
}

.job-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.job-card {
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  border: 1px solid $border-color;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    border-color: $primary-color;
    box-shadow: $shadow-md;
  }
}

.job-main-info {
  margin-bottom: $spacing-md;
}

.job-header-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: $spacing-md;
  margin-bottom: $spacing-sm;
}

.job-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0;
}

.job-salary-row {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  margin-bottom: $spacing-sm;
}

.job-salary {
  font-size: $font-size-xl;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.job-settlement {
  font-size: $font-size-sm;
  color: $text-muted;
  padding: 2px 8px;
  background: $bg-tertiary;
  border-radius: $radius-sm;
}

.job-meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-md;
}

.job-location,
.job-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: $font-size-sm;
  color: $text-muted;
}

.job-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: $spacing-md;
  border-top: 1px solid $border-light;
}

.job-publisher {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $text-secondary;
}

.job-publish-time {
  color: $text-light;
}

.job-actions {
  display: flex;
  gap: $spacing-sm;
}

.job-action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  color: $text-muted;
  background: transparent;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
}

.job-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-md;
  margin-top: $spacing-xl;
}

.page-btn {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-primary;
  border: 1px solid $border-color;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover:not(:disabled) {
    color: $primary-color;
    border-color: $primary-color;
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.page-info {
  font-size: $font-size-sm;
  color: $text-muted;
}

.job-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.hot-item {
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.hot-info {
  flex: 1;
}

.hot-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.hot-salary {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

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

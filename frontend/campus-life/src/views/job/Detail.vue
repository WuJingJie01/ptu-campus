<template>
  <MainLayout>
    <div class="detail-page">
      <div class="detail-container">
        <div class="detail-main">
          <Card class="job-card">
            <div class="job-header">
              <div class="job-title-row">
                <h1 class="job-title">{{ job.title }}</h1>
                <Badge :type="job.urgent ? 'danger' : 'success'">
                  {{ job.urgent ? '急招' : '招聘中' }}
                </Badge>
              </div>
              <div class="job-salary-row">
                <span class="job-salary">{{ job.salary }}</span>
                <span class="job-settlement">{{ job.settlement }}</span>
              </div>
            </div>

            <div class="job-info-list">
              <div class="job-info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                  <circle cx="12" cy="10" r="3" />
                </svg>
                <span class="info-label">工作地点</span>
                <span class="info-value">{{ job.location }}</span>
              </div>
              <div class="job-info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10" />
                  <polyline points="12 6 12 12 16 14" />
                </svg>
                <span class="info-label">工作时间</span>
                <span class="info-value">{{ job.workTime }}</span>
              </div>
              <div class="job-info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="1" y="4" width="22" height="16" rx="2" ry="2" />
                  <line x1="1" y1="10" x2="23" y2="10" />
                </svg>
                <span class="info-label">结算方式</span>
                <span class="info-value">{{ job.paymentMethod }}</span>
              </div>
              <div class="job-info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10" />
                  <polyline points="12 6 12 12 16 14" />
                </svg>
                <span class="info-label">结算时间</span>
                <span class="info-value">{{ job.paymentTime }}</span>
              </div>
            </div>

            <div class="job-description">
              <h3 class="section-title">工作内容</h3>
              <div class="description-content">
                <p v-for="(para, index) in job.description" :key="index">{{ para }}</p>
              </div>
            </div>

            <div class="job-requirements">
              <h3 class="section-title">任职要求</h3>
              <ul class="requirements-list">
                <li v-for="(req, index) in job.requirements" :key="index">{{ req }}</li>
              </ul>
            </div>

            <div class="job-actions">
              <Button type="primary" size="lg" @click="handleMessage">
                私信发布者
              </Button>
              <button class="action-icon-btn" :class="{ 'action-icon-btn-active': job.collected }" @click="handleCollect">
                <svg width="20" height="20" viewBox="0 0 24 24" :fill="job.collected ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                  <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                </svg>
              </button>
              <button class="action-icon-btn" @click="handleShare">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="18" cy="5" r="3" />
                  <circle cx="6" cy="12" r="3" />
                  <circle cx="18" cy="19" r="3" />
                  <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" />
                  <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" />
                </svg>
              </button>
              <button class="action-icon-btn" @click="showReportModal = true">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
                  <line x1="4" y1="22" x2="4" y2="15" />
                </svg>
              </button>
            </div>
          </Card>
        </div>

        <aside class="detail-sidebar">
          <Card title="发布者信息" class="sidebar-card">
            <div class="publisher-card">
              <Avatar :src="job.publisher.avatar" :name="job.publisher.name" size="xl" />
              <h4 class="publisher-name">{{ job.publisher.name }}</h4>
              <p v-if="job.publisher.college" class="publisher-info">
                {{ job.publisher.college }} · {{ job.publisher.grade }}
              </p>
              <div class="publisher-stats">
                <div class="publisher-stat">
                  <span class="publisher-stat-value">{{ job.publisher.jobsCount }}</span>
                  <span class="publisher-stat-label">发布兼职</span>
                </div>
              </div>
              <Button type="primary" block @click="router.push(`/user/${job.publisher.id}`)">
                查看主页
              </Button>
            </div>
          </Card>

          <Card title="相关推荐" class="sidebar-card">
            <div class="related-list">
              <div
                v-for="item in relatedJobs"
                :key="item.id"
                class="related-item"
                @click="router.push(`/job/${item.id}`)"
              >
                <h5 class="related-title">{{ item.title }}</h5>
                <div class="related-salary">{{ item.salary }}</div>
              </div>
            </div>
          </Card>
        </aside>
      </div>
    </div>

    <Modal v-model="showReportModal" title="举报兼职" size="sm">
      <div class="report-form">
        <div class="report-options">
          <label v-for="option in reportOptions" :key="option" class="report-option">
            <input type="radio" v-model="reportType" :value="option" />
            <span>{{ option }}</span>
          </label>
        </div>
        <textarea
          v-model="reportReason"
          placeholder="请详细描述举报原因..."
          rows="4"
          class="report-textarea"
        ></textarea>
      </div>
      <template #footer>
        <Button type="ghost" @click="showReportModal = false">取消</Button>
        <Button type="primary" :disabled="!reportType || !reportReason" @click="submitReport">
          提交举报
        </Button>
      </template>
    </Modal>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Button, Avatar, Badge, Modal } from '@/components'

const router = useRouter()
const showReportModal = ref(false)
const reportType = ref('')
const reportReason = ref('')

const reportOptions = ['虚假信息', '已招满', '薪资不符', '广告', '其他']

const job = ref({
  id: 1,
  title: '图书馆助理管理员',
  salary: '15元/小时',
  settlement: '月结',
  location: '莆田学院图书馆',
  workTime: '周一至周五 18:00-21:00',
  paymentMethod: '银行转账',
  paymentTime: '每月15日结算上月工资',
  urgent: true,
  description: [
    '负责图书馆自习室的秩序维护，确保安静的学习环境。',
    '协助图书上架、整理工作，保持书架整洁有序。',
    '解答读者的基本咨询，引导读者使用图书馆资源。',
    '完成图书馆安排的其他临时性工作。'
  ],
  requirements: [
    '莆田学院在校大学生',
    '工作认真负责，有耐心',
    '每周至少能工作3个班次',
    '有图书馆工作经验者优先'
  ],
  publisher: {
    id: 1,
    name: '图书馆',
    avatar: '',
    college: '',
    grade: '',
    jobsCount: 5
  },
  collected: false
})

const relatedJobs = ref([
  { id: 2, title: '食堂窗口兼职', salary: '12元/小时' },
  { id: 3, title: '家教辅导（数学）', salary: '50元/小时' },
  { id: 4, title: '活动志愿者', salary: '50元/天' }
])

const handleMessage = () => {
  router.push('/message')
}

const handleCollect = () => {
  job.value.collected = !job.value.collected
}

const handleShare = () => {
  navigator.clipboard.writeText(window.location.href)
  alert('链接已复制到剪贴板')
}

const submitReport = () => {
  alert('举报已提交，等待审核')
  showReportModal.value = false
}
</script>

<style lang="scss" scoped>
.detail-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.detail-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 320px;
  }
}

.job-card {
  margin-bottom: 0;
}

.job-header {
  margin-bottom: $spacing-xl;
}

.job-title-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: $spacing-md;
  margin-bottom: $spacing-md;
}

.job-title {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0;
}

.job-salary-row {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.job-salary {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.job-settlement {
  font-size: $font-size-sm;
  color: $text-muted;
  padding: 4px 12px;
  background: $bg-tertiary;
  border-radius: $radius-full;
}

.job-info-list {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-tertiary;
  border-radius: $radius-lg;
  margin-bottom: $spacing-xl;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.job-info-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  svg {
    color: $primary-color;
    flex-shrink: 0;
  }
}

.info-label {
  font-size: $font-size-sm;
  color: $text-muted;
  min-width: 60px;
}

.info-value {
  font-size: $font-size-sm;
  color: $text-primary;
}

.section-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-md;
}

.job-description,
.job-requirements {
  margin-bottom: $spacing-xl;
}

.description-content {
  p {
    font-size: $font-size-base;
    color: $text-secondary;
    line-height: $line-height-relaxed;
    margin: 0 0 $spacing-md;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.requirements-list {
  padding-left: $spacing-lg;
  margin: 0;
  
  li {
    font-size: $font-size-base;
    color: $text-secondary;
    line-height: $line-height-relaxed;
    margin-bottom: $spacing-sm;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.job-actions {
  display: flex;
  gap: $spacing-md;
  flex-wrap: wrap;
  padding-top: $spacing-lg;
  border-top: 1px solid $border-color;
}

.action-icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  color: $text-muted;
  background: $bg-tertiary;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
  
  &-active {
    color: $primary-color;
  }
}

.detail-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.publisher-card {
  text-align: center;
}

.publisher-name {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: $spacing-md 0 $spacing-xs;
}

.publisher-info {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0 0 $spacing-md;
}

.publisher-stats {
  display: flex;
  justify-content: center;
  gap: $spacing-xl;
  margin-bottom: $spacing-md;
}

.publisher-stat {
  text-align: center;
}

.publisher-stat-value {
  display: block;
  font-size: $font-size-lg;
  font-weight: $font-weight-bold;
  color: $text-primary;
}

.publisher-stat-label {
  font-size: $font-size-xs;
  color: $text-muted;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.related-item {
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.related-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.related-salary {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.report-form {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.report-options {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.report-option {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $text-secondary;
  cursor: pointer;
}

.report-textarea {
  width: 100%;
  padding: $spacing-md;
  font-size: $font-size-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-lg;
  resize: none;
  outline: none;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
}
</style>

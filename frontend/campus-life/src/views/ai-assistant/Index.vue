<template>
  <div class="ai-layout">
    <Navbar />
    <div class="ai-page">
      <div class="ai-container">
        <div class="chat-area">
          <div class="chat-messages" ref="messagesRef">
            <div v-if="messages.length === 0" class="welcome-section">
              <div class="welcome-avatar">
                <div class="avatar-glow"></div>
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="10" />
                  <path d="M8 14s1.5 2 4 2 4-2 4-2" />
                  <line x1="9" y1="9" x2="9.01" y2="9" />
                  <line x1="15" y1="9" x2="15.01" y2="9" />
                </svg>
              </div>
              <h2 class="welcome-title">你好，我是AI学长</h2>
              <p class="welcome-desc">我可以帮你解答关于校园生活、学习、就业等方面的问题</p>
              
              <div class="quick-actions">
                <button
                  v-for="action in quickActions"
                  :key="action.text"
                  class="quick-action-btn"
                  @click="sendQuickQuestion(action.text)"
                >
                  <span class="quick-action-icon" v-html="action.icon"></span>
                  <span class="quick-action-text">{{ action.text }}</span>
                </button>
              </div>
            </div>

            <div
              v-for="(message, index) in messages"
              :key="message.id"
              :class="['message', `message-${message.role}`]"
            >
              <div v-if="message.role === 'assistant'" class="message-avatar">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="10" />
                  <path d="M8 14s1.5 2 4 2 4-2 4-2" />
                  <line x1="9" y1="9" x2="9.01" y2="9" />
                  <line x1="15" y1="9" x2="15.01" y2="9" />
                </svg>
              </div>
              <div class="message-content">
                <div class="message-text" v-html="formatMessage(message.content)"></div>
                <div class="message-actions" v-if="message.role === 'assistant'">
                  <button class="action-btn" @click="copyMessage(message.content)" title="复制">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                      <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                    </svg>
                  </button>
                  <button class="action-btn" @click="regenerate(message)" title="重新生成">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <polyline points="23 4 23 10 17 10" />
                      <polyline points="1 20 1 14 7 14" />
                      <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>

            <div v-if="isTyping" class="message message-assistant">
              <div class="message-avatar">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="10" />
                  <path d="M8 14s1.5 2 4 2 4-2 4-2" />
                  <line x1="9" y1="9" x2="9.01" y2="9" />
                  <line x1="15" y1="9" x2="15.01" y2="9" />
                </svg>
              </div>
              <div class="message-content">
                <div class="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          </div>

          <div class="input-area">
            <div class="input-container">
              <textarea
                v-model="inputMessage"
                placeholder="输入你的问题，我来帮你解答..."
                rows="1"
                @keydown.enter.exact.prevent="sendMessage"
                @input="autoResize"
                ref="textareaRef"
              ></textarea>
              <button
                class="send-btn"
                :class="{ 'send-btn-active': inputMessage.trim() && !isTyping }"
                :disabled="!inputMessage.trim() || isTyping"
                @click="sendMessage"
              >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="22" y1="2" x2="11" y2="13" />
                  <polygon points="22 2 15 22 11 13 2 9 22 2" />
                </svg>
              </button>
            </div>
            <p class="input-hint">AI学长可能会产生不准确的信息，请注意甄别</p>
          </div>
        </div>

        <aside class="sidebar">
          <div class="sidebar-section">
            <h3 class="sidebar-title">新对话</h3>
            <button class="new-chat-btn" @click="clearChat">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              开始新对话
            </button>
          </div>

          <div class="sidebar-section">
            <h3 class="sidebar-title">推荐问题</h3>
            <div class="suggested-questions">
              <button
                v-for="question in suggestedQuestions"
                :key="question"
                class="suggested-btn"
                @click="sendQuickQuestion(question)"
              >
                {{ question }}
              </button>
            </div>
          </div>

          <div class="sidebar-section">
            <h3 class="sidebar-title">能力范围</h3>
            <ul class="capability-list">
              <li>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12" />
                </svg>
                校园生活问题解答
              </li>
              <li>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12" />
                </svg>
                学习方法建议
              </li>
              <li>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12" />
                </svg>
                就业方向指导
              </li>
              <li>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12" />
                </svg>
                校园规章制度
              </li>
            </ul>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/layout/Navbar.vue'

const messagesRef = ref<HTMLElement | null>(null)
const textareaRef = ref<HTMLTextAreaElement | null>(null)
const inputMessage = ref('')
const isTyping = ref(false)

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
}

const messages = ref<Message[]>([])

const quickActions = [
  { text: '图书馆怎么预约座位？', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" /><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" /></svg>' },
  { text: '食堂营业时间是什么？', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg>' },
  { text: '如何申请勤工助学？', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" /><circle cx="12" cy="7" r="4" /></svg>' },
  { text: '选修课怎么选？', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 10v6M2 10l10-5 10 5-10 5z" /><path d="M6 12v5c3 3 9 3 12 0v-5" /></svg>' }
]

const suggestedQuestions = [
  '毕业学分要求是多少？',
  '宿舍网络怎么连接？',
  '如何办理校园卡挂失？',
  '奖学金申请条件？'
]

const responses: Record<string, string> = {
  '图书馆怎么预约座位？': `图书馆座位预约有以下几种方式：

**方式一：微信公众号预约**
1. 关注"莆田学院图书馆"公众号
2. 点击"座位预约"菜单
3. 选择日期、时段和座位
4. 确认预约

**方式二：现场预约机**
图书馆大厅设有自助预约机，可现场选择座位。

**注意事项：**
- 预约成功后请在30分钟内签到
- 连续3次未签到将被限制预约权限
- 如无法到场请提前取消预约

还有其他问题吗？😊`,
  
  '食堂营业时间是什么？': `莆田学院各食堂营业时间如下：

**第一食堂**
- 早餐：6:30 - 9:00
- 午餐：11:00 - 13:00
- 晚餐：17:00 - 19:00

**第二食堂**
- 早餐：6:30 - 9:30
- 午餐：10:30 - 13:30
- 晚餐：16:30 - 19:30

**第三食堂（风味餐厅）**
- 全天：10:00 - 21:00

建议避开12:00-12:30的高峰期，用餐体验更好哦！🍽️`,

  '如何申请勤工助学？': `勤工助学申请流程如下：

**申请条件**
- 家庭经济困难学生优先
- 学有余力，不影响学业
- 品行端正，无违纪记录

**申请步骤**
1. 开学初关注学工处通知
2. 登录学生工作管理系统
3. 填写《勤工助学申请表》
4. 提交家庭经济情况证明
5. 等待学院审核公示

**岗位类型**
- 图书馆助理
- 实验室助理
- 行政助理
- 校园保洁等

报酬按小时计算，一般12-15元/小时。祝你申请顺利！💪`,

  '选修课怎么选？': `选修课选课指南：

**选课时间**
- 通常在每学期第16-17周
- 具体时间关注教务处通知

**选课步骤**
1. 登录教务管理系统
2. 点击"网上选课"
3. 选择"通识选修课"
4. 浏览课程信息，选择心仪课程
5. 确认提交

**选课技巧**
- 提前了解课程评价
- 准备备选方案（热门课程竞争激烈）
- 注意学分要求（一般需修满8-10学分）
- 合理安排上课时间

**课程分类**
- 人文社科类
- 自然科学类
- 艺术体育类
- 创新创业类

选课当天系统可能拥堵，建议提前登录等待！📚`
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const autoResize = () => {
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
    textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 150) + 'px'
  }
}

const formatMessage = (content: string) => {
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>')
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isTyping.value) return

  const userMessage: Message = {
    id: Date.now(),
    role: 'user',
    content: inputMessage.value.trim()
  }
  
  messages.value.push(userMessage)
  inputMessage.value = ''
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
  }
  scrollToBottom()

  isTyping.value = true
  
  await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 1000))
  
  const response = responses[userMessage.content] || generateResponse(userMessage.content)
  
  const assistantMessage: Message = {
    id: Date.now() + 1,
    role: 'assistant',
    content: response
  }
  
  messages.value.push(assistantMessage)
  isTyping.value = false
  scrollToBottom()
}

const generateResponse = (question: string) => {
  const templates = [
    `关于"${question}"这个问题，我来帮你解答：

根据莆田学院的相关规定，建议你：

1. **咨询相关部门** - 可以直接到相关部门咨询，获取最准确的信息
2. **查看学校官网** - 学校官网通常有详细的政策说明
3. **联系辅导员** - 辅导员可以提供针对性的指导

如果还有其他问题，欢迎继续问我！😊`,
    
    `这是一个很好的问题！

关于${question}，我了解到：

- 学校有相关的政策和服务
- 建议通过官方渠道获取最新信息
- 如有特殊情况，可以申请相关服务

需要我提供更详细的信息吗？🤔`
  ]
  
  return templates[Math.floor(Math.random() * templates.length)]
}

const sendQuickQuestion = (question: string) => {
  inputMessage.value = question
  sendMessage()
}

const clearChat = () => {
  messages.value = []
}

const copyMessage = (content: string) => {
  navigator.clipboard.writeText(content)
  alert('已复制到剪贴板')
}

const regenerate = (message: Message) => {
  const index = messages.value.findIndex(m => m.id === message.id)
  if (index > 0) {
    const userMessage = messages.value[index - 1]
    if (userMessage.role === 'user') {
      messages.value = messages.value.slice(0, index)
      inputMessage.value = userMessage.content
      sendMessage()
    }
  }
}
</script>

<style lang="scss" scoped>
.ai-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.ai-page {
  height: calc(100vh - 64px);
  margin-top: 64px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
}

.ai-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 0;
  height: 100%;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr;
  }
}

.chat-area {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
  background: $bg-primary;
}

.chat-messages {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: $spacing-xl;
  
  &::-webkit-scrollbar {
    width: 6px;
  }
  
  &::-webkit-scrollbar-thumb {
    background: $border-color;
    border-radius: 3px;
  }
}

.welcome-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  text-align: center;
  padding: $spacing-2xl;
}

.welcome-avatar {
  position: relative;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  color: white;
  margin-bottom: $spacing-xl;
}

.avatar-glow {
  position: absolute;
  inset: -4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  opacity: 0.3;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.1); opacity: 0.1; }
}

.welcome-title {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0 0 $spacing-sm;
}

.welcome-desc {
  font-size: $font-size-base;
  color: $text-muted;
  margin: 0 0 $spacing-xl;
  max-width: 400px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
  max-width: 500px;
  width: 100%;
  
  @include respond-to(sm) {
    grid-template-columns: 1fr;
  }
}

.quick-action-btn {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-md $spacing-lg;
  background: $bg-secondary;
  border: 1px solid $border-color;
  border-radius: $radius-xl;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: left;
  
  &:hover {
    background: $bg-tertiary;
    border-color: #667eea;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
  }
}

.quick-action-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: $radius-lg;
  color: white;
  flex-shrink: 0;
}

.quick-action-text {
  font-size: $font-size-sm;
  color: $text-secondary;
  line-height: 1.4;
}

.message {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message-user {
  flex-direction: row-reverse;
  
  .message-content {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: $radius-xl $radius-xl 4px $radius-xl;
  }
}

.message-assistant {
  .message-content {
    background: $bg-secondary;
    border-radius: $radius-xl $radius-xl $radius-xl 4px;
  }
}

.message-avatar {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  color: white;
  flex-shrink: 0;
}

.message-content {
  max-width: 70%;
  padding: $spacing-md $spacing-lg;
}

.message-text {
  font-size: $font-size-base;
  line-height: 1.7;
  
  :deep(strong) {
    font-weight: $font-weight-semibold;
  }
}

.message-actions {
  display: flex;
  gap: $spacing-sm;
  margin-top: $spacing-sm;
  opacity: 0;
  transition: opacity 0.2s ease;
  
  .message-content:hover & {
    opacity: 1;
  }
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  background: transparent;
  border: none;
  border-radius: $radius-md;
  color: $text-muted;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: $bg-tertiary;
    color: $text-secondary;
  }
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: $spacing-sm;
  
  span {
    width: 8px;
    height: 8px;
    background: $text-muted;
    border-radius: 50%;
    animation: typing 1.4s infinite ease-in-out;
    
    &:nth-child(1) { animation-delay: 0s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.4s; }
  }
}

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-6px); }
}

.input-area {
  flex-shrink: 0;
  padding: $spacing-lg $spacing-xl;
  border-top: 1px solid $border-light;
  background: $bg-primary;
}

.input-container {
  display: flex;
  align-items: flex-end;
  gap: $spacing-md;
  padding: $spacing-md;
  background: $bg-secondary;
  border: 1px solid $border-color;
  border-radius: $radius-xl;
  transition: all 0.2s ease;
  
  &:focus-within {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }
  
  textarea {
    flex: 1;
    padding: $spacing-sm;
    font-size: $font-size-base;
    background: transparent;
    border: none;
    resize: none;
    outline: none;
    max-height: 150px;
    line-height: 1.5;
    
    &::placeholder {
      color: $text-light;
    }
  }
}

.send-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: $bg-tertiary;
  border: none;
  border-radius: 50%;
  color: $text-muted;
  cursor: pointer;
  transition: all 0.2s ease;
  flex-shrink: 0;
  
  &:disabled {
    cursor: not-allowed;
  }
  
  &-active {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    
    &:hover {
      transform: scale(1.05);
    }
  }
}

.input-hint {
  font-size: $font-size-xs;
  color: $text-light;
  text-align: center;
  margin: $spacing-sm 0 0;
}

.sidebar {
  background: $bg-secondary;
  border-left: 1px solid $border-light;
  padding: $spacing-lg;
  overflow-y: auto;
  
  @include respond-to(lg) {
    display: none;
  }
}

.sidebar-section {
  margin-bottom: $spacing-xl;
}

.sidebar-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-semibold;
  color: $text-muted;
  margin: 0 0 $spacing-md;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.new-chat-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-sm;
  width: 100%;
  padding: $spacing-md;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: $radius-lg;
  color: white;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
}

.suggested-questions {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.suggested-btn {
  padding: $spacing-sm $spacing-md;
  background: $bg-primary;
  border: 1px solid $border-color;
  border-radius: $radius-lg;
  font-size: $font-size-sm;
  color: $text-secondary;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    border-color: #667eea;
    color: #667eea;
  }
}

.capability-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
  margin: 0;
  padding: 0;
  
  li {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    font-size: $font-size-sm;
    color: $text-secondary;
    
    svg {
      color: #667eea;
      flex-shrink: 0;
    }
  }
}
</style>

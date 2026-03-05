<template>
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-main">
        <div class="footer-brand">
          <div class="footer-logo">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
            </svg>
          </div>
          <h3 class="footer-title">莆田学院校园生活系统</h3>
          <p class="footer-desc">为莆田学院师生提供便捷的校园生活服务平台</p>
        </div>
        
        <div class="footer-links">
          <div class="footer-links-group">
            <h4 class="footer-links-title">功能模块</h4>
            <router-link to="/forum" class="footer-link">校园论坛</router-link>
            <router-link to="/market" class="footer-link">校园市场</router-link>
            <router-link to="/job" class="footer-link">校园兼职</router-link>
            <router-link to="/ai-assistant" class="footer-link">AI学长</router-link>
          </div>
          
          <div class="footer-links-group">
            <h4 class="footer-links-title">帮助中心</h4>
            <a href="#" class="footer-link">使用指南</a>
            <a href="#" class="footer-link">常见问题</a>
            <a href="#" class="footer-link">联系我们</a>
            <a href="#" class="footer-link">意见反馈</a>
          </div>
          
          <div class="footer-links-group">
            <h4 class="footer-links-title">关于我们</h4>
            <a href="#" class="footer-link">关于系统</a>
            <a href="#" class="footer-link">用户协议</a>
            <a href="#" class="footer-link">隐私政策</a>
            <a href="#" class="footer-link">免责声明</a>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <p class="footer-copyright">
          © {{ currentYear }} 莆田学院校园生活系统 版权所有
        </p>
        <p class="footer-icp">仅供学习交流使用</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const currentYear = new Date().getFullYear()
</script>

<style lang="scss" scoped>
.footer {
  background: $bg-dark;
  color: $text-light;
  padding: $spacing-2xl 0 $spacing-lg;
}

.footer-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
}

.footer-main {
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  padding-bottom: $spacing-xl;
  border-bottom: 1px solid rgba($text-light, 0.1);
  
  @include respond-to(md) {
    grid-template-columns: 1.5fr 2fr;
  }
}

.footer-brand {
  max-width: 300px;
}

.footer-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, $primary-color, $primary-light);
  border-radius: $radius-lg;
  color: $text-white;
  margin-bottom: $spacing-md;
}

.footer-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-white;
  margin: 0 0 $spacing-sm;
}

.footer-desc {
  font-size: $font-size-sm;
  color: $text-light;
  margin: 0;
  line-height: $line-height-relaxed;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-lg;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.footer-links-group {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.footer-links-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-semibold;
  color: $text-white;
  margin: 0 0 $spacing-sm;
}

.footer-link {
  font-size: $font-size-sm;
  color: $text-light;
  text-decoration: none;
  transition: color $transition-fast;
  
  &:hover {
    color: $primary-light;
  }
}

.footer-bottom {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-xs;
  padding-top: $spacing-lg;
  text-align: center;
  
  @include respond-to(md) {
    flex-direction: row;
    justify-content: space-between;
  }
}

.footer-copyright,
.footer-icp {
  font-size: $font-size-xs;
  color: $text-muted;
  margin: 0;
}
</style>

<template>
  <span :class="['badge', `badge-${type}`, `badge-${size}`]">
    <slot />
  </span>
</template>

<script setup lang="ts">
interface Props {
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default'
  size?: 'sm' | 'md' | 'lg'
}

withDefaults(defineProps<Props>(), {
  type: 'default',
  size: 'md',
})
</script>

<style lang="scss" scoped>
.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: $font-weight-medium;
  border-radius: $radius-full;
  white-space: nowrap;
  
  &-sm {
    padding: 2px 8px;
    font-size: $font-size-xs;
  }
  
  &-md {
    padding: 4px 12px;
    font-size: $font-size-xs;
  }
  
  &-lg {
    padding: 6px 16px;
    font-size: $font-size-sm;
  }
  
  &-default {
    background: $bg-tertiary;
    color: $text-secondary;
  }
  
  &-primary {
    background: rgba($primary-color, 0.1);
    color: $primary-color;
  }
  
  &-success {
    background: rgba($success-color, 0.1);
    color: $success-color;
  }
  
  &-warning {
    background: rgba($warning-color, 0.1);
    color: $warning-color;
  }
  
  &-danger {
    background: rgba($danger-color, 0.1);
    color: $danger-color;
  }
  
  &-info {
    background: rgba($info-color, 0.1);
    color: $info-color;
  }
}
</style>

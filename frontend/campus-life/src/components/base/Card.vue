<template>
  <div :class="['card', `card-${variant}`, { 'card-clickable': clickable, 'card-hoverable': hoverable }]">
    <div v-if="$slots.header || title" class="card-header">
      <slot name="header">
        <h3 v-if="title" class="card-title">{{ title }}</h3>
      </slot>
    </div>
    <div class="card-body">
      <slot />
    </div>
    <div v-if="$slots.footer" class="card-footer">
      <slot name="footer" />
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  title?: string
  variant?: 'default' | 'bordered' | 'elevated'
  clickable?: boolean
  hoverable?: boolean
}

withDefaults(defineProps<Props>(), {
  title: '',
  variant: 'default',
  clickable: false,
  hoverable: false,
})
</script>

<style lang="scss" scoped>
.card {
  @include card-base;
  
  &-default {
    background: $bg-primary;
  }
  
  &-bordered {
    background: $bg-primary;
    border: 1px solid $border-color;
    box-shadow: none;
  }
  
  &-elevated {
    background: $bg-primary;
    box-shadow: $shadow-md;
    border: none;
  }
  
  &-clickable {
    cursor: pointer;
  }
  
  &-hoverable {
    transition: all $transition-base;
    
    &:hover {
      box-shadow: $shadow-lg;
      transform: translateY(-2px);
    }
  }
}

.card-header {
  padding: $spacing-md $spacing-lg;
  border-bottom: 1px solid $border-color;
}

.card-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0;
}

.card-body {
  padding: $spacing-lg;
}

.card-footer {
  padding: $spacing-md $spacing-lg;
  border-top: 1px solid $border-color;
  background: $bg-secondary;
  border-radius: 0 0 $radius-lg $radius-lg;
}
</style>

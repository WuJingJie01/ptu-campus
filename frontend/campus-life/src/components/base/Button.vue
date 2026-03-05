<template>
  <button
    :class="['btn', `btn-${type}`, `btn-${size}`, { 'btn-loading': loading, 'btn-disabled': disabled }]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <span v-if="loading" class="btn-spinner"></span>
    <slot v-else />
  </button>
</template>

<script setup lang="ts">
interface Props {
  type?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'md',
  loading: false,
  disabled: false,
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style lang="scss" scoped>
.btn {
  @include button-base;
  
  &-sm {
    padding: $spacing-xs $spacing-sm;
    font-size: $font-size-xs;
  }
  
  &-md {
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
  }
  
  &-lg {
    padding: $spacing-md $spacing-lg;
    font-size: $font-size-base;
  }
  
  &-primary {
    background: $primary-color;
    color: $text-white;
    
    &:hover:not(:disabled) {
      background: $primary-dark;
    }
  }
  
  &-secondary {
    background: $secondary-color;
    color: $text-white;
    
    &:hover:not(:disabled) {
      background: $secondary-dark;
    }
  }
  
  &-outline {
    background: transparent;
    border: 1px solid $primary-color;
    color: $primary-color;
    
    &:hover:not(:disabled) {
      background: $primary-bg;
    }
  }
  
  &-ghost {
    background: transparent;
    color: $text-secondary;
    
    &:hover:not(:disabled) {
      background: $bg-tertiary;
    }
  }
  
  &-danger {
    background: $danger-color;
    color: $text-white;
    
    &:hover:not(:disabled) {
      background: $danger-dark;
    }
  }
  
  &-loading {
    cursor: wait;
  }
  
  &-disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.btn-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid currentColor;
  border-right-color: transparent;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>

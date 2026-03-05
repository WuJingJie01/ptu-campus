<template>
  <div class="input-wrapper">
    <label v-if="label" :for="inputId" class="input-label">
      {{ label }}
      <span v-if="required" class="input-required">*</span>
    </label>
    <div class="input-container">
      <span v-if="$slots.prefix" class="input-prefix">
        <slot name="prefix" />
      </span>
      <input
        :id="inputId"
        v-model="inputValue"
        :type="type"
        :placeholder="placeholder"
        :disabled="disabled"
        :readonly="readonly"
        :maxlength="maxlength"
        class="input"
        :class="[`input-${size}`, { 'input-error': error, 'input-with-prefix': $slots.prefix, 'input-with-suffix': $slots.suffix }]"
        @focus="handleFocus"
        @blur="handleBlur"
        @input="handleInput"
      />
      <span v-if="$slots.suffix" class="input-suffix">
        <slot name="suffix" />
      </span>
    </div>
    <p v-if="error" class="input-error-message">{{ error }}</p>
    <p v-else-if="hint" class="input-hint">{{ hint }}</p>
  </div>
</template>

<script setup lang="ts">
interface Props {
  modelValue?: string | number
  label?: string
  type?: 'text' | 'password' | 'email' | 'number' | 'search'
  placeholder?: string
  size?: 'sm' | 'md' | 'lg'
  disabled?: boolean
  readonly?: boolean
  required?: boolean
  maxlength?: number
  error?: string
  hint?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  type: 'text',
  placeholder: '',
  size: 'md',
  disabled: false,
  readonly: false,
  required: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: string | number]
  focus: [event: FocusEvent]
  blur: [event: FocusEvent]
  input: [event: Event]
}>()

const inputId = `input-${Math.random().toString(36).slice(2, 9)}`

const inputValue = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const handleFocus = (event: FocusEvent) => {
  emit('focus', event)
}

const handleBlur = (event: FocusEvent) => {
  emit('blur', event)
}

const handleInput = (event: Event) => {
  emit('input', event)
}
</script>

<style lang="scss" scoped>
.input-wrapper {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.input-label {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-secondary;
}

.input-required {
  color: $danger-color;
  margin-left: 2px;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.input {
  @include input-base;
  
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
  
  &-with-prefix {
    padding-left: $spacing-xl;
  }
  
  &-with-suffix {
    padding-right: $spacing-xl;
  }
  
  &-error {
    border-color: $danger-color;
    
    &:focus {
      border-color: $danger-color;
      box-shadow: 0 0 0 3px rgba($danger-color, 0.1);
    }
  }
  
  &:disabled {
    background: $bg-tertiary;
    cursor: not-allowed;
  }
}

.input-prefix,
.input-suffix {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $text-muted;
}

.input-prefix {
  left: $spacing-md;
}

.input-suffix {
  right: $spacing-md;
}

.input-error-message {
  font-size: $font-size-xs;
  color: $danger-color;
}

.input-hint {
  font-size: $font-size-xs;
  color: $text-muted;
}
</style>

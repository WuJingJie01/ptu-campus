import { ref, computed } from 'vue'

export function useCountDown() {
  const count = ref(0)
  const timer = ref<number | null>(null)
  
  const isCounting = computed(() => count.value > 0)
  
  const startCountDown = (seconds: number = 60) => {
    if (isCounting.value) return
    
    count.value = seconds
    timer.value = window.setInterval(() => {
      count.value--
      if (count.value <= 0) {
        stopCountDown()
      }
    }, 1000)
  }
  
  const stopCountDown = () => {
    if (timer.value) {
      clearInterval(timer.value)
      timer.value = null
    }
    count.value = 0
  }
  
  return {
    count,
    isCounting,
    startCountDown,
    stopCountDown
  }
}

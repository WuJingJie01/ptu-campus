import { defineStore } from 'pinia'

interface User {
  id: number
  username: string
  nickname: string
  avatar: string
  email: string
  isVerified: boolean
  college?: string
  grade?: string
  fansCount: number
  followCount: number
}

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token') || '',
    isLoggedIn: false,
  }),

  getters: {
    userInfo: (state) => state.user,
    isAuth: (state) => state.isLoggedIn && !!state.token,
  },

  actions: {
    setUser(user: User) {
      this.user = user
      this.isLoggedIn = true
    },

    setToken(token: string) {
      this.token = token
      localStorage.setItem('token', token)
    },

    logout() {
      this.user = null
      this.token = ''
      this.isLoggedIn = false
      localStorage.removeItem('token')
    },

    updateUserInfo(info: Partial<User>) {
      if (this.user) {
        this.user = { ...this.user, ...info }
      }
    },
  },
})

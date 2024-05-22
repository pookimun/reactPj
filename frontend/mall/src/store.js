import { configureStore } from '@reduxjs/toolkit'
import loginSlice from './slices/loginSlice' // p353 추가
import cartSlice from './slices/cartSlice' // 471 추가

export default configureStore({
  reducer: { 
    "loginSlice": loginSlice, // p353 추가
    "cartSlice" : cartSlice // 471 추가
  }
})
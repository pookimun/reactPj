import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getCartItems, postChangeCart } from "../api/cartApi";

export const getCartItemsAsync = createAsyncThunk('getCartItemsAsync', () => {
    // 카트 아이템을 가져오는 메서드 
    return getCartItems()
    // Thunk(고려된후) 비동기 호출의 상태에 따른 결과를 처리 함
    // 장바구니의 경우 초기 상태는 빈 배열을 이용하고 api 서버의 호출 결과는 모두 장바구니 아이템들의 배열이므로 
    // 이를 상태 데이터로 보관 

    // 장바구니 상태는 로그인과 달리 모든 데이터는 api 서버에 의존함
    // 로그인을 한 순간 api 서버로부터 현재 사용자의 장바구니 아이템을 가져오고 사용자가 리액트 화면에서 변경하면
    // 즉각 서버와 동기화 될 필요가 있음.(db에 즉각 반영)
})

export const postChangeCartAsync = createAsyncThunk('postCartItemsAsync', (param) => {
    // post 방식의 카트 변경
    return postChangeCart(param)

})

const initState = []

const cartSlice = createSlice({
  name: 'cartSlice',
  initialState: initState,

  extraReducers: (builder) => {
    builder.addCase( 
      getCartItemsAsync.fulfilled, (state, action) => { 
        console.log("getCartItemsAsync fulfilled")

        return action.payload
      }
    )
    .addCase(
      postChangeCartAsync.fulfilled, (state, action) => {

        console.log("postCartItemsAsync fulfilled")

        return action.payload
      }
    )
  }
})

export default cartSlice.reducer
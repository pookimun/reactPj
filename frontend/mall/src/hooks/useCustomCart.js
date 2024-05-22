// 531 제거 import { useDispatch, useSelector } from "react-redux"
import { getCartItemsAsync, postChangeCartAsync } from "../slices/cartSlice"
// useMutation을 통해서 서버에 장바구니 아이템을 조정하고 useQuery를 이용해서 리액트 내에 데이터를 보관하게 만듬.
// 다른 컴포넌트들이 장바구니 데이터를 공유해서 사용할 수 도 있기 때문에 이부분은 리코일로 만든 cartState를 이용함.
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"  // 531 추가 
import { getCartItems, postChangeCart } from "../api/cartApi" // 531 추가 
import { useRecoilState } from "recoil" // 531 추가 
import { cartState } from "../atoms/cartState" // 531 추가 
import { useEffect } from "react" // 531 추가 

const useCustomCart = () => {
  // 531 추가 ~
  const [cartItems,setCartItems] = useRecoilState(cartState)

  const queryClient = useQueryClient()

  const changeMutation = useMutation((param)  => postChangeCart(param), {onSuccess: (result) => {
    setCartItems(result)
  }})

  const query = useQuery(["cart"], getCartItems, {staleTime: 1000 * 60 * 60}) // 1 hour 
  // 1시간의 staleTime을 지정한 이유는 외부에서 어떤 영향으로 상품의 정보가 변경될 수 있기 대문에 자주는 아니지만, 가끔 장바구니안에 있는 상품정보를 다시 가져오기 위함
  useEffect(() => {

    if(query.isSuccess || changeMutation.isSuccess ) {

      queryClient.invalidateQueries("cart")
      setCartItems(query.data)

    }


  },[query.isSuccess, query.data])

  // 531 제거 const cartItems = useSelector(state => state.cartSlice)

  // 531 제거 const dispatch = useDispatch()

  // 531 제거 const refreshCart = () => {

  // 531 제거   dispatch(getCartItemsAsync())

  // 531 제거 }

  const changeCart = (param) => {
    changeMutation.mutate(param) // 532 추가
    // 532 제거 dispatch(postChangeCartAsync(param))
  }

  return  {cartItems,  changeCart} // 532 제거 refreshCart, 

}

export default useCustomCart

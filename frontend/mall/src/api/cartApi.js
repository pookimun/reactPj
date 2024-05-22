import jwtAxios from "../util/jwtUtil" // 현재 사용자의 로그인 정보를 활용함.
import { API_SERVER_HOST } from "./todoApi"

const host = `${API_SERVER_HOST}/api/cart`

export const getCartItems = async ( ) => {
    // http://localhost:3000/api/cart/items 호출시 아이템 조회
    const res = await jwtAxios.get(`${host}/items`)

    return res.data

}

export const postChangeCart = async (cartItem) => {
    // http://localhost:3000/api/cart/change/카트아이템 호출시 아이템 추가, 수량 변경
    const res = await jwtAxios.post(`${host}/change`, cartItem)

    return res.data
}
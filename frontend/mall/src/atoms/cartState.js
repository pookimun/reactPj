import { atom, selector } from "recoil";  // 530 추가, selector
// 카드에 담긴 장바구니 아이템의 배열 선언

export const cartState = atom({
  key:'cartState',
  default:[]
})

// Atom이 데이터 자체를 의미 한다면 Seletor는 데이터를 이용해 처리 할 수 있는 기능을 의미
// 장바구니의 경우 해당 상품의 가격과 수량을 이용해서 전체 장바구니의 총액을 구하는 기능을 사용함
// 리코일의 selector는 데이터를 가공해서 원하는 기능을 제공함. getter 처럼 사용됨
// Atom으로 관리되는 데이터를 변경하는 setter의 기능도 같이 사용할 수 있음.
// 531 추가 ~
export const cartTotalState = selector( {
  key: "cartTotalState",
  get: ( {get} ) => {

    const arr = get(cartState)

    const initialValue = 0

    const total = arr.reduce((total , current) => total + current.price * current.qty , initialValue)

    return total
  }
})

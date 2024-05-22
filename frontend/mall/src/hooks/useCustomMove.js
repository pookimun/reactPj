import { useState } from "react" //p153 추가  
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom"

//다시 목록화면으로 이동하는 기능 추가
//공통적인 코드의 경우 커스텀 훅을 이용
// useNavigate, useSearchParams를 이용해야 함 -> 수정/삭제 화면에서도 동일

const getNum  = (param, defaultValue) => {

    if(!param){
      return defaultValue
    }
  
    return parseInt(param)
  }

  const useCustomMove = () => {
    // useNavigate, useSearchParams를 이용해서 원하는 기능을 moveToList로 만들고 이를 page, size와 함께 반환
    // useNavigate를 이용하게 되고 반환된 데이터들 중에서 필요한 데이터만 선별해서 사용가능

    const navigate = useNavigate()
  
    const [refresh, setRefresh] = useState(false) //p153 추가
  
    const [queryParams] = useSearchParams()
  
    const page = getNum(queryParams.get('page'), 1)
    const size = getNum(queryParams.get('size'),10)
  
    const queryDefault = createSearchParams({page, size}).toString() //새로 추가
  
    //list 버튼용
    const moveToList = (pageParam) => {
  
      let queryStr = ""
  
      if(pageParam){
  
        const pageNum = getNum(pageParam.page, 1)
        const sizeNum = getNum(pageParam.size, 10)
  
        queryStr = createSearchParams({page:pageNum, size: sizeNum}).toString()
      }else {
        queryStr = queryDefault
      }
   
      setRefresh(!refresh) // p153추가  

      navigate({
        pathname: `../list`,
        search:queryStr
      })
    }
    // p141 수정버튼 용
    const moveToModify = (num) => {

        console.log(queryDefault)
    
        navigate({
          pathname: `../modify/${num}`,
          search: queryDefault  //수정시에 기존의 쿼리 스트링 유지를 위해 
        })
      }

      // p155 읽기용 
      const moveToRead =(num) => {

        console.log(queryDefault)
    
        navigate({
          pathname: `../read/${num}`,
          search: queryDefault
        })
      }





  return  {moveToList, moveToModify, moveToRead, page, size, refresh} // p141 moveToModify추가, p154 refresh 추가 

}

export default useCustomMove
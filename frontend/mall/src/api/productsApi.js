import axios from "axios"
import { API_SERVER_HOST } from "./todoApi"
import jwtAxios from "../util/jwtUtil"

const host = `${API_SERVER_HOST}/api/products`

export const postAdd = async (product) => {

    // Axios가 기본적으로 json을 이용하기 때문에 해더에 파일 업로드용 헤더로 설정을 추가해야 한다.
    const header = {headers: {"Content-Type": "multipart/form-data"}}

    // 경로 뒤 '/' 주의 
    const res = await jwtAxios.post(`${host}/`, product, header)

return res.data

}

// p265 리스트 가져오기
export const getList = async ( pageParam ) => {

    const {page,size} = pageParam
  
    const res = await jwtAxios.get(`${host}/list`, {params: {page:page,size:size }})
    
    return res.data
  
}

  //p273 getOne 한개의 값 가져오기
export const getOne = async (tno) => {

  // p390 변경 const res = await axios.get(`${host}/${tno}` )
  const res = await jwtAxios.get(`${host}/${tno}` )
  return res.data
  
}

// p281 추가
export const putOne = async (pno, product) => {

    const header = {headers: {"Content-Type": "multipart/form-data"}}
  
    const res = await jwtAxios.put(`${host}/${pno}`, product, header)
  
    return res.data
  
  }
  
  export const deleteOne = async (pno) => {
  
    const res = await jwtAxios.delete(`${host}/${pno}`)
  
    return res.data
  
  }
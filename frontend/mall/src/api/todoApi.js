import axios from "axios" 
import jwtAxios from "../util/jwtUtil"
//  npm install -no-fund axios 설치 오류시 

//서버 주소

export const API_SERVER_HOST = 'http://localhost:8080'

const prefix = `${API_SERVER_HOST}/api/todo`

export const getOne = async (tno) => {

  // p390 변경 const res = await axios.get(`${prefix}/${tno}` )  axios -> jwtAxios 모두 변경
  const res = await jwtAxios.get(`${prefix}/${tno}` )
  return res.data

}

export const getList = async ( pageParam ) => {

  const {page,size} = pageParam

  const res = await jwtAxios.get(`${prefix}/list`, {params: {page:page,size:size }})
  
  return res.data

}


export const postAdd = async (todoObj) => {

  const res = await jwtAxios.post(`${prefix}/` , todoObj)

  return res.data
}


export const deleteOne = async (tno) => {

  const res = await jwtAxios.delete(`${prefix}/${tno}` )

  return res.data

}

export const putOne = async (todo) => {

  const res = await jwtAxios.put(`${prefix}/${todo.tno}`, todo)

  return res.data
}


import { useEffect, useState } from "react"
import  {getOne} from "../../api/todoApi"
import useCustomMove from "../../hooks/useCustomMove" // p139 추가 hooks용

// 특정한 번호의 값에 의해서 todoApi.js의 getOne()을 호출하도록 구성
// useEffect()를 사용해서 번호가 변경되었을 때만 Axios를 이용하는 getOne()을 호출하도록 구성
// 비동기 통신으로 가져온 데이터는 컴포넌트 상태로 반영
const initState = {
    tno:0,
    title:'',
    writer: '',
    dueDate: null,
    complete: false
  }

  const ReadComponent = ({tno}) => {

    const [todo, setTodo] = useState(initState) //아직 todo는 사용하지 않음 
    
    const {moveToList, moveToModify} = useCustomMove() // p142추가 moveToModify
  
    useEffect(() => {
      getOne(tno).then(data => {
        console.log(data)
        setTodo(data)
      })    
    }, [tno])
  
    return (  
        <div className = "border-2 border-sky-200 mt-10 m-2 p-4 ">
      
        {makeDiv('Tno', todo.tno)}
        {makeDiv('Writer', todo.writer)}
        {makeDiv('Title', todo.title)}
        {makeDiv('Due Date', todo.dueDate)}
        {makeDiv('Complete', todo.complete ? 'Completed' : 'Not Yet')}
        
            {/* 버튼 추가 p140.........start */}
            <div className="flex justify-end p-4">

                <button type="button" className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500" 
                onClick={() => moveToList()}>
                List
                </button>
            
                <button type="button" 
                className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
                onClick={() => moveToModify(tno)}>
                Modify
                </button>
            </div> 
        </div> //p136 추가 (출력할 때 공통적인 스타일과 구성이 많음, 컴포넌트는 아니지만 JSX를 반환하는 함수를 이용해 코드양을 줄임)
  )
}

const makeDiv = (title,value) =>       
<div className="flex justify-center">
  <div className="relative mb-4 flex w-full flex-wrap items-stretch">
    <div className="w-1/5 p-6 text-right font-bold">{title}</div>
    <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
    {value}        
    </div>
  </div>
</div>  //p136 makeDiv 함수 추가 (출력할 때 공통적인 스타일과 구성이 많음, 컴포넌트는 아니지만 JSX를 반환하는 함수를 이용해 코드양을 줄임)

export default ReadComponent
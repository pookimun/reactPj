import { useParams } from "react-router-dom"; // p172 변경useNavigate
import ModifyComponent from "../../components/todo/ModifyComponent";

const ModifyPage = () => { // p172 제거 {tno}

  const {tno} = useParams() // p172 추가 

    // p172 제거 const navigate = useNavigate()

    //  const moveToRead = () => {

    // navigate({pathname:`/todo/read/${tno}`})

    //  }

    // const moveToList = () => {

    //     navigate({pathname:`/todo/list`})

    // }



    return ( 

      <div className="p-4 w-full bg-white">  //p172 추가
        <div className="text-3xl font-extrabold">
          Todo Modify Page 
        </div> 

      <ModifyComponent tno={tno}/>   //p172 추가
      </div>
       );
    }
     
    export default ModifyPage;
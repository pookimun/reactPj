import ListComponent from "../../components/products/ListComponent";

const ListPage = () => {
    // p244 추가
    return (
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
            Products List Page
            </div>
            {/*  p267 추가 */}
            <ListComponent/> 

        </div>


    )



}

export default ListPage;
import { ReactComponent as ArrowIcon } from 'assets/images/arrow.svg';
import ReactPaginate from 'react-paginate';

import './styles.css';

const Pagination = () => {
  return (
    
      <ReactPaginate 
      pageCount={10}
      pageRangeDisplayed={3}
      marginPagesDisplayed={1}
      containerClassName="pagination-container"
      pageLinkClassName="pagination-intem"
      breakClassName="pagination-item"
      previousClassName="arrow-previous"
      previousLabel={<ArrowIcon/>}
      nextClassName="arrow-next"
      nextLabel={<ArrowIcon/>}
      activeLinkClassName="pagination-link-active"
      disabledClassName="arrow-inactive"
      
      />

      
  );
};

export default Pagination;

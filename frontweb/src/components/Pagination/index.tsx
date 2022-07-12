import { ReactComponent as ArrowIcon } from 'assets/images/arrow.svg';

import './styles.css';

const Pagination = () => {
    return(
        <div className="pagination-container">
            <ArrowIcon className="arrow-previous arrow-inactive" />
            <div className="pagination-intem active">1</div>
            <div className="pagination-intem">2</div>
            <div className="pagination-intem">3</div>
            <div className="pagination-intem">...</div>
            <div className="pagination-intem">10</div>
            <ArrowIcon className="arrow-next arrow-active" />
            
        </div>
    );
}

export default Pagination;
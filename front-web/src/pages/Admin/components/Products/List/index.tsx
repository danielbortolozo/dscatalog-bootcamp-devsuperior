import Pagination from 'core/components/Pagination';
import { ProductsResponse } from 'core/types/Product';
import { makePrivateRequest, makeRequest } from 'core/utils/request';
import { useCallback, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { toast } from 'react-toastify';
import Card from '../Card';
import CardLoader from '../Loaders/ProductCardLoader';

const List = () => {

  const [productsResponse, setProductsResponse] = useState<ProductsResponse>();
  const [isLoading, setIsLoading] = useState(false);
  const [activePage, setActivePage] = useState(0);
  const history = useHistory();
 
 

  const getProducts = useCallback(() => {
    const params = {
      page: activePage,
      linesPerPage: 4,
      direction: 'DESC',
      orderBy: 'id'
    }
    setIsLoading(true);
    makeRequest({ url: '/products', params })

      .then(response => { 
        setProductsResponse(response.data)          
      })
      .finally(() => {
        setIsLoading(false);
      })
  }, [activePage]);

  useEffect(() => {
    getProducts();
  }, [getProducts]);


  const handleCreate = () => {

    history.push('/admin/products/create');
  }
  const onRemove = (productId: number) => {
    const confirm = window.confirm('Deseja realmente excluir este produto?');

    if (confirm) {
        makePrivateRequest({
          url: `/products/${productId}`,
          method: 'DELETE'
        })
          .then(() => {
            getMessageSuccess();
            history.push('/admin/products');
            getProducts();
          }).catch(() => {
            toast.error('Error ao excluir um produto!!!')
          })
    }
  }
  const getMessageSuccess = () => {
    return toast.warning('Produto excluido com sucesso!', {
      position: "top-center",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined
    });
  }

  return (
    <div className="admin-products-list">
     <div className="d-flex flex-row col-12">
        <button className="btn btn-primary btn-lg" onClick={handleCreate}>
          Adicionar
        </button>     
        <label  className="total-registro">
          <h5> Total registro: {productsResponse?.totalElements} </h5>
        </label>    
      </div>

      <div className="admin-list-container">
      {isLoading ? <CardLoader /> : (
        productsResponse?.content.map(product => (
          <Card product={product} key={product.id} onRemove={onRemove} />
        ))
      )}
        
      </div>
      { productsResponse && (
        <Pagination
          totalPages={productsResponse.totalPages}
          activePage={activePage}
          onChange={page => setActivePage(page)}
        />

      )}

    </div>
  );
};


export default List;
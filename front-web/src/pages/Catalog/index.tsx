import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './styles.scss';
import ProductCard from './components/ProductCard';
import { makeRequest } from '../../core/utils/request';
import { ProductsResponse } from '../../core/types/Product';
import { Link } from 'react-router-dom';


const Catalog = () => {
 // quando o componente iniciar, buscar a lista de produtos.
 // 
 const [productsResponse, setProductsResponse] = useState<ProductsResponse>();
 useEffect(() => {
   const params = {
     page: 0,
     linesPerPage: 12
   }
   console.log('buscando products ...');
   makeRequest({url: '/products', params})
     
     .then(response => setProductsResponse(response.data))
 }, []);
   return (
    <div className="catalog-container">
      <h1 className="catalog-title">
        Catálago de produtos
      </h1>
      <div className="catalog-products">
        {productsResponse?.content.map(product => (
          <Link to={`/products/${product.id}`} key={product.id}>
            <ProductCard product={product}/>
          </Link>  
        ))}                
      </div>  
    </div> 
  );

}
  
  export default Catalog;
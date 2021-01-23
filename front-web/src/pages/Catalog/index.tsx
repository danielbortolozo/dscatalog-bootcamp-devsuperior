import React from 'react';
import './styles.scss';
import ProductCard from './components/ProductCard';


const Catalog = () => (
    <div className="catalog-container">
      <h1 className="catalog-title">
        Catálago de produtos
      </h1>
      <div className="catalog-products">
         <ProductCard/>
         <ProductCard/>
         <ProductCard/>
         <ProductCard/>
         <ProductCard/>
         <ProductCard/>
         <ProductCard/>
         
       
      </div>  


    </div>
     
  
  );
  
  export default Catalog;
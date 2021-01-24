import React from 'react';
import { ReactComponent as ProductImg } from '../../../../core/assets/images/img-product.svg';
import ProductPrice from '../../../../core/components/productprice';
import './styles.scss';


const ProductCard = () => (

    <div className="card-base border-radius-10 product-card" >
        <ProductImg />
        <div className="product-info" >
            <h6 className="product-name">
                Computador Desktop - Intel Core i7
           </h6>
            <ProductPrice price="2.889,90"/>
        </div>

    </div>

);

export default ProductCard;
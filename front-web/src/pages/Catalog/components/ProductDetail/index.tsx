import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { ReactComponent as ArrowIcon } from '../../../../core/assets/images/arrow.svg';
import { ReactComponent as ImgProduct } from '../../../../core/assets/images/img-product.svg';
import ProductPrice from '../../../../core/components/productprice';

import './styles.scss';

type ParamsType = {
    productId: string;
}

const ProductDetail = () => {
    const { productId } = useParams<ParamsType>();
    return (
        <div className="product-details-container">
            <div className="card-base border-radius-20 product-details">
                <Link to="/products" className="products-detail-link-goback">
                    <ArrowIcon className="icon-goback" />
                    <h1 className="text-goback"> voltar</h1>
                </Link>
                <div className="row">
                    <div className="col-6 pr-5">
                        <div className="product-details-card text-center">
                            <ImgProduct className="product-details-img" />
                        </div>
                        <h1 className="product-details-name">Computador Desktop processador i7   </h1>
                       
                        <ProductPrice price="2.789,90" />
                    </div>

                    <div className="col-6 product-details-card">
                        <h1 className="product-description-title">
                            Descrição do produto
                    </h1>
                        <p className="product-description-text">
                            Seja um mestre em multitarefas com a capacidade para exibir
                            quatro aplicativos simultâneos na tela.
                            A tela está ficando abarrotada?
                            Crie áreas de trabalho virtuais para obter mais
                            espaço e trabalhar com os itens que você deseja.
                            Além disso, todas as notificações e principais configurações são
                            reunidas em uma única tela de fácil acesso.

                    </p>

                    </div>

                </div>
            </div>
        </div>
    );

};



export default ProductDetail;
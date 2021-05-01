import { makePrivateRequest, makeRequest } from 'core/utils/request';
import { useForm, Controller } from 'react-hook-form';
import BaseForm from '../../Baseform';
import { toast } from 'react-toastify';
import './styles.scss';
import { useHistory, useParams } from 'react-router';
import { useEffect, useState } from 'react';
import { Category } from 'core/types/Product';
import Select from 'react-select';
import PriceField from './PriceField';
import ImageUpload from '../ImageUpload';

export type FormState = {
    name: string;
    price: string;
    imgUrl: string;
    description: string;
    categories: Category[];
    date: Date;
}
type ParamsType = {
    productId: string;
}

const Form = () => {

    const { register, handleSubmit, formState: { errors }, setValue, control } = useForm<FormState>();
    //   const [hasError, setHasError] = useState(false);
    const history = useHistory();
    // let location = useLocation<LocationState>();
    const { productId } = useParams<ParamsType>();

    const [categories, setCategories] = useState<Category[]>([]);
    const [isLoadingCategories, setIsLoadingCategories] = useState(false);
    const [uploadedImgUrl, setUploadedImgUrl] = useState('');
    const [productImgUrl, setProductImgUrl] = useState('');

    
    const isEditing = productId !== 'create';
    const formTitle = isEditing ? 'Alterar produto' : 'Cadastrar produto'

   
    //  const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        //   setIsLoading(true)
        if (isEditing) {
            makeRequest({ url: `/products/${productId}` })
                .then(response => {
                    setValue('name', response.data.name);
                    setValue('price', response.data.price);
                    setValue('description', response.data.description);                   
                    setValue('categories', response.data.categories);
                   
                    setProductImgUrl(response.data.imgUrl);   
                    
                    
                })
        }
        
    
        //     .finally(() => setIsLoading(false))
    }, [productId, isEditing, setValue]);

    useEffect(() => {
        setIsLoadingCategories(true);
      makeRequest({ url:'/categories' })
      .then(response => {
                    
        setCategories(response.data.content)})
      .finally(() => setIsLoadingCategories(false))
        
    }, [setCategories, setIsLoadingCategories]);

    const onSubmit = (data: FormState) => {
        
        const payload = {
            ...data,
            imgUrl: uploadedImgUrl || productImgUrl,
            date: new Date()
        }
       
        makePrivateRequest({
            url: isEditing ? `/products/${productId}` : '/products',
            method: isEditing ? 'PUT' : 'POST',
            data: payload
        }).then(() => {
            getMessageSuccess();
            history.push('/admin/products');
        }).catch(() => {
            toast.error('Error ao salvar um produto!!!')
        });
    }

    const onUploadSuccess = (imgUrl: string) => {
        console.log('onUploadSuccess', imgUrl);
        
       setUploadedImgUrl(imgUrl);
    }

    const getMessageSuccess = () => {
        return toast.success('Produto cadastrado com sucesso!', {
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
                
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm title={formTitle}>
                <div className="row">
                    <div className="col-6">
                        <div className="margin-botton-30">
                            <input
                               name="name"
                               ref={register({
                                required: "Campo obrigatório",
                                minLength: {
                                    value: 5,
                                    message: "O campo deve ter no mínimo 5 caracteres."
                                },
                                maxLength: {
                                    value: 60,
                                    message: "O campo deve ter no máximo 60 caracteres."
                                }
                            })}
                                type="text"
                                className={`form-control input-base ${errors.name ? 'is-invalid' : ''} `}
                                placeholder={"nome do produto"}
                            />
                            {errors.name && (
                                <div className="invalid-feedback d-block">
                                    {errors.name?.message}
                                </div>
                            )}
                        </div>
                        <div className="margin-botton-30">
                            <Controller
                               as={Select} 
                               
                               name="categories"
                               rules={{ required: true}}
                               control={control}
                               isLoading={isLoadingCategories}
                               classNamePrefix="categories-select"
                               isMulti    
                               getOptionLabel={ (option: Category) => option.name }   
                               getOptionValue={ (option: Category) => String(option.id) }                   
                               options={categories}
                               placeholder="Categorias"
                               defaultValue=""
                            />
                            {errors.categories && (
                                <div className="invalid-feedback d-block">
                                    Campo obrigatório
                                </div>
                            )}

                        </div>    

                        <div className="margin-botton-30">
                            <PriceField control={control} />
                            {errors.price && (
                                <div className="invalid-feedback d-block">
                                    {errors.price?.message}
                                </div>
                            )}
                        </div>
                        
                        <div className="margin-botton-30">
                          <ImageUpload 
                             onUploadSuccess={onUploadSuccess} 
                             productImgUrl={productImgUrl}   
                          />
                        </div>
                    </div>

                    <div className="col-6">
                        <div>
                            <textarea
                                name="description"
                                ref={register({ required: "Campo obrigatório" })}
                                className="form-control input-base"
                                cols={30}
                                rows={10}
                            />
                            {errors.description && (
                                <div className="invalid-feedback d-block">
                                    {errors.description?.message}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}

export default Form;
import { makeRequest } from 'core/utils/request';
import React, { useState } from 'react';
import BaseForm from '../../Baseform';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    category: string;
    description: string;
}
type FormEvent = React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>;

const Form = () => {
    const [formData, setformData] = useState<FormState>({
        name: '',
        price: '',
        category: '',
        description: ''
    });
    

    const handleOnChange = (event: FormEvent) => {
       const name = event.target.name;
       const value = event.target.value;
        
       setformData(data => ({...data, [name]: value }));
    }
    
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) =>{
        event.preventDefault();
       
        const payload = {
            ...formData,
            imgUrl: 'https://www.techinn.com/f/13777/137776929/microsoft-xbox-series-x-1tb.jpg',

            categories: [{ id: formData.category }]
        }

        makeRequest({ url: '/products', method: 'POST', data: payload})
        console.log(payload);
    }
  
    return (
        <form onSubmit={handleSubmit}> 
            <BaseForm title="cadastrar produto">
                <div className="row">
                    <div className="col-6">
                        <input 
                            value={formData.name}
                            name="name"
                            type="text" 
                            className="form-control mb-5"
                            onChange={handleOnChange} 
                            placeholder={"nome do produto"}
                        />
                        <select                               
                            className="form-control mb-5"
                            onChange={handleOnChange}
                            name="category"
                            value={formData.category}
                            >
                            <option value="1">Livros</option>      
                            <option value="3">Computares</option>      
                            <option value="2">Eletronicos</option>                                   
                        </select> 

                        <input  name="price" 
                                value={formData.price}
                                type="text" 
                                className="form-control mb-5"
                                onChange={handleOnChange} 
                                placeholder={"price"}
                        />   
                    </div>    
                    <div className="col-6">
                       <textarea 
                           className="form-control"
                           name="description" 
                           value={formData.description}
                           onChange={handleOnChange}                        
                           cols={30}
                           rows={10}
                        />     
                        
                    </div>       
                </div>    
            </BaseForm>
        </form>
    );
}

export default Form;
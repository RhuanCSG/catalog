import './styles.css';
import { ReactComponent as SearchIcon } from 'assets/images/searchIcon.svg';
import { Category } from 'types/category';
import { Controller, useForm } from 'react-hook-form';
import Select from 'react-select';
import { useEffect, useState } from 'react';
import { requestBackend } from 'util/requests';

type ProductFilterData = {
  name: string;
  category: Category;
};

const ProductFilter = () => {
  const [selectCategories, setSelectCategories] = useState<Category[]>([]);
  const { register, handleSubmit, control } = useForm<ProductFilterData>();

  const onSubmit = (formData: ProductFilterData) => {
    console.log('ENVIOU', formData);
  };

  useEffect(() => {
    requestBackend({ url: '/categories' }).then((response) => {
      setSelectCategories(response.data.content);
    });
  }, []);

  return (
    <div className="base-card product-filter-container">
      <form onSubmit={handleSubmit(onSubmit)} className="product-filter-form">
        <div className="product-filter-name-container">
          <input
            {...register('name', {
              required: 'Campo obrigatório',
            })}
            type="text"
            className={`form-control base-input`}
            placeholder="Nome do produto"
            name="name"
          />
          <button className="product-filter-button-search-icon">
            <SearchIcon />
          </button>
        </div>

        <div className="product-filter-bottom-container">
          <div className="product-filter-category-container">
            <Controller
              name="category"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  options={selectCategories}
                  isClearable
                  placeholder="Categoria"
                  classNamePrefix="product-filter-select"
                  getOptionLabel={(category: Category) => category.name}
                  getOptionValue={(category: Category) => String(category.id)}
                />
              )}
            />
          </div>
          <button className="btn btn-outline-secondary btn-product-filter-clear">LIMPAR <span className="btn-product-filter-word">FILTRO</span> </button>
        </div>
      </form>
    </div>
  );
};

export default ProductFilter;

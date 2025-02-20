import React from 'react';

const Categories = () => {

    return(
    <aside className = "categories">
        <div className = "category-list">
     <h3>Categories</h3>
        <button className = "category-btn active">Web development</button>
        <button className = "category-btn">IoT</button>
        <button className = "category-btn">AWS</button>
        <button className = "category-btn">Data science</button>
        </div>
    </aside>
    )
}

export default Categories;
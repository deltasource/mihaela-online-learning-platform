import React from 'react';

const Header = () => {  
    return(
        <header>
            <div className = "logo"> Elearning platform</div>
            <nav>
                <button className = "btn-primary">Log in</button>
                <button className = "btn-primary">Sign up</button>
            </nav>
        </header>
    )
}

export default Header;
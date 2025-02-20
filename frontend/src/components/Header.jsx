import React from 'react';

const Header = () => {  
    return(
        <header>
            <div className = "logo"> Elearn</div>
            <nav>
                <button className = "btn-primary">Log in</button>
                <button className = "btn-primary">Sign up</button>
            </nav>
        </header>
    )
}

export default Header;
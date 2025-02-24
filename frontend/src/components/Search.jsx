import { Container, InputAdornment, TextField } from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"

const Search = () => {
    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <TextField
                fullWidth
                placeholder="Search..."
                InputProps={{
                    startAdornment: (
                        <InputAdornment position="start">
                            <SearchIcon />
                        </InputAdornment>
                    ),
                }}
            />
        </Container>
    )
}

export default Search

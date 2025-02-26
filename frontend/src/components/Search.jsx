import { Box, InputAdornment, TextField } from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"

const Search = () => {
    return (
        <Box sx={{ py: 4, textAlign: "center" }}>
            <TextField
                placeholder="Search for courses..."
                variant="outlined"
                sx={{ width: "100%", maxWidth: 600 }}
                InputProps={{
                    startAdornment: (
                        <InputAdornment position="start">
                            <SearchIcon />
                        </InputAdornment>
                    ),
                }}
            />
        </Box>
    )
}

export default Search

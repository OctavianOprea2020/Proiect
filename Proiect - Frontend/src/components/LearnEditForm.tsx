import { Button } from "@mui/material";
import { Box } from "@mui/system"
import { FC } from "react"

export type LearnEditFormProps = {
    text: string;
    value?: number;
    callParent: (text: string) => void;
}

const LearnEditForm: FC<LearnEditFormProps> = ({ text, value, callParent }) => {

    return <Box>This is an edit form containing {text}
        <Button onClick={() => callParent(text)}>Tell something to the parent component</Button>
    </Box>
}

export default LearnEditForm;
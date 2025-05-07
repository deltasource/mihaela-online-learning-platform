import type { UUID } from "./common"

export interface Video {
    title: any;
    id?: UUID
    fileName: string
    filePath: string
    courseId: UUID
}

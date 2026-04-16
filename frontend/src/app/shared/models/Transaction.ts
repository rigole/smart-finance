
export interface Transaction {
    id?: string;
    amount: number;
    type: string;
    date: Date;
    description: string;
}
export type ConfirmType = 'danger' | 'warning' | 'info';

export interface ConfirmDialogData {
    title?: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
    type?: ConfirmType;
}
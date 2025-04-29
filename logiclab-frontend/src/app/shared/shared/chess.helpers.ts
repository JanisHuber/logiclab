import { ChessFigure } from '../../core/models/chess/logic/chessfigure';

export interface Position {
    row: number;
    col: number;
}

export function jsonToChessBoard(jsonBoard: string): (ChessFigure | null)[][] {
    const board: (ChessFigure | null)[][] = Array.from({ length: 8 }, () => Array(8).fill(null));
    
    const parsedBoard = JSON.parse(jsonBoard);
    parsedBoard.forEach((cell: any) => {
        // Konvertiere die Buchstaben-Koordinate in eine Zahl (A=0, B=1, etc.)
        const rowIndex = cell.row.toUpperCase().charCodeAt(0) - 'A'.charCodeAt(0);
        const row = 7 - rowIndex; // Invertiere die Reihe für die korrekte Darstellung
        const col = cell.column - 1;
        
        if (cell.figure) {
            board[row][col] = new ChessFigure(
                cell.figure.type,
                cell.figure.figureColor,
                `${cell.row}${cell.column}`,
                cell.figure.value
            );
        } else {
            board[row][col] = null;
        }
    });

    return board;
}

export function boardToApiPosition(row: number, col: number): { convertedRow: string, convertedColumn: number } {
    const convertedRow = String.fromCharCode('A'.charCodeAt(0) + (7 - row));
    const convertedColumn = col + 1;

    return { convertedRow: convertedRow, convertedColumn: convertedColumn };
}

export function apiPositionToBoard(position: { convertedRow: string, convertedColumn: number }): { row: number, column: number } {
    const row = 7 - (position.convertedRow.charCodeAt(0) - 'A'.charCodeAt(0));
    const column = position.convertedColumn - 1;
    return { row: row, column: column };
}
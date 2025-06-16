export interface CommentaireDto {
id: number;
  contenu: string;
  createurUsername: string | null;
  date: string | null;
}

export interface ArticleDto {
  id: number;
  titre: string;
  contenu: string;
  date: string; // ISO string
  themeName: string;
  auteurUsername: string;
  commentaires: CommentaireDto[];
}
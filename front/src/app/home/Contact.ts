export interface Contact {
  id : number;
  lastName : string;
  firstName: string;
  email: string;
  categories: Set<Categorie>;
}
export interface Categorie{
  id: number;
  nom: string;
  description: string;
}


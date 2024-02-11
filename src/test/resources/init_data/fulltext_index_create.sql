alter table post add fulltext key fx_title (title) with parser ngram;
show index from post;

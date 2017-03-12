$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoTitulo = button.data('codigo');
	var descricaoTitulo = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir <strong>' + descricaoTitulo + '</strong>?');
});


$('#editarEccModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	
	var uri = button.data('uri');
	var idEcc = button.data('idECC')
	var escore = button.data('escore')
	var status = button.data('status')
	var dataInclusao = button.data('dataInclusao')
	var descricaoTitulo = button.data('descricao');

	
	$(".ecc_id").val(idEcc);
	$(".ecc_status").val(status);
	$(".ecc_dataInclusao").val(dataInclusao);
	$(".ecc_escore").val(escore);
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + uri);

	modal.find('.modal-title').html('Alterar <strong>' + descricaoTitulo + '</strong> para...' );
});



$('#editarPesoModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	
	var uri = button.data('uri');
	var idPeso = button.data('idPeso')
	var peso = button.data('peso')
	var status = button.data('status')
	var dataInclusao = button.data('dataInclusao')
	var dataPesagem = button.data('dataPesagem')
	var descricaoTitulo = button.data('descricao');

	
	$(".peso_id").val(idPeso);
	$(".peso_status").val(status);
	$(".peso_dataInclusao").val(dataInclusao);
	$(".peso_dataPesagem").val(dataPesagem);
	$(".peso_peso").val(peso);
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + uri);

	modal.find('.modal-title').html('Alterar o peso <strong>' + descricaoTitulo + '</strong> para...' );
});





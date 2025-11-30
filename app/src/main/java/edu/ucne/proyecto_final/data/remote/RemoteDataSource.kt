package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import edu.ucne.proyecto_final.data.remote.usuario.UsuarioApi
import edu.ucne.proyecto_final.dto.remote.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
private val usuarioApi: UsuarioApi,
    private val categoriaApi: CategoriaApi,
    private val clienteApi: ClienteApi,
    private val clienteDetalleApi: ClienteDetalleApi,
    private val proveedorApi: ProveedorApi,
    private val compraApi: CompraApi,
    private val compraDetalleApi: CompraDetalleApi,
    private val insumoApi: InsumoApi,
    private val insumoDetalleApi: InsumoDetalleApi,
    private val reclamoApi: ReclamoApi
) {
    // ------------------------- USUARIOS -------------------------
    suspend fun getUsuarios() = usuarioApi.getUsuarios()
    suspend fun getUsuario(id: Int) = usuarioApi.getUsuario(id)
    suspend fun createUsuario(data: UsuarioDto) = usuarioApi.createUsuario(data)
    suspend fun updateUsuario(id: Int, data: UsuarioDto) = usuarioApi.updateUsuario(id, data)
    suspend fun deleteUsuario(id: Int) = usuarioApi.deleteUsuario(id)
    // Categoria
    suspend fun getCategorias(): List<CategoriaDto> = categoriaApi.getCategorias()
    suspend fun getCategoria(id: Int): CategoriaDto = categoriaApi.getCategoria(id)
    suspend fun createCategoria(categoria: CategoriaDto): CategoriaDto = categoriaApi.createCategoria(categoria)
    suspend fun updateCategoria(id: Int, categoria: CategoriaDto): CategoriaDto = categoriaApi.updateCategoria(id, categoria)
    suspend fun deleteCategoria(id: Int) = categoriaApi.deleteCategoria(id)

    // Cliente
    suspend fun getClientes(): List<ClienteDto> = clienteApi.getClientes()
    suspend fun getCliente(id: Int): ClienteDto = clienteApi.getCliente(id)
    suspend fun createCliente(cliente: ClienteDto): ClienteDto = clienteApi.createCliente(cliente)
    suspend fun updateCliente(id: Int, cliente: ClienteDto): ClienteDto = clienteApi.updateCliente(id, cliente)
    suspend fun deleteCliente(id: Int) = clienteApi.deleteCliente(id)

    // ClienteDetalle
    suspend fun getClienteDetalles(): List<ClienteDetalleDto> = clienteDetalleApi.getClienteDetalles()
    suspend fun getClienteDetalle(id: Int): ClienteDetalleDto = clienteDetalleApi.getClienteDetalle(id)
    suspend fun createClienteDetalle(detalle: ClienteDetalleDto): ClienteDetalleDto = clienteDetalleApi.createClienteDetalle(detalle)
    suspend fun updateClienteDetalle(id: Int, detalle: ClienteDetalleDto): ClienteDetalleDto = clienteDetalleApi.updateClienteDetalle(id, detalle)
    suspend fun deleteClienteDetalle(id: Int) = clienteDetalleApi.deleteClienteDetalle(id)

    // Proveedor
    suspend fun getProveedores(): List<ProveedorDto> = proveedorApi.getProveedores()
    suspend fun getProveedor(id: Int): ProveedorDto = proveedorApi.getProveedor(id)
    suspend fun createProveedor(proveedor: ProveedorDto): ProveedorDto = proveedorApi.createProveedor(proveedor)
    suspend fun updateProveedor(id: Int, proveedor: ProveedorDto): ProveedorDto = proveedorApi.updateProveedor(id, proveedor)
    suspend fun deleteProveedor(id: Int) = proveedorApi.deleteProveedor(id)

    // Compra
    suspend fun getCompras(): List<CompraDto> = compraApi.getCompras()
    suspend fun getCompra(id: Int): CompraDto = compraApi.getCompra(id)
    suspend fun createCompra(compra: CompraDto): CompraDto = compraApi.createCompra(compra)
    suspend fun updateCompra(id: Int, compra: CompraDto): CompraDto = compraApi.updateCompra(id, compra)
    suspend fun deleteCompra(id: Int) = compraApi.deleteCompra(id)

    // CompraDetalle
    suspend fun getCompraDetalles(): List<CompraDetalleDto> = compraDetalleApi.getCompraDetalles()
    suspend fun getCompraDetalle(id: Int): CompraDetalleDto = compraDetalleApi.getCompraDetalle(id)
    suspend fun createCompraDetalle(detalle: CompraDetalleDto): CompraDetalleDto = compraDetalleApi.createCompraDetalle(detalle)
    suspend fun updateCompraDetalle(id: Int, detalle: CompraDetalleDto): CompraDetalleDto = compraDetalleApi.updateCompraDetalle(id, detalle)
    suspend fun deleteCompraDetalle(id: Int) = compraDetalleApi.deleteCompraDetalle(id)

    // Insumo
    suspend fun getInsumos(): List<InsumoDto> = insumoApi.getInsumos()
    suspend fun getInsumo(id: Int): InsumoDto = insumoApi.getInsumo(id)
    suspend fun createInsumo(insumo: InsumoDto): InsumoDto = insumoApi.createInsumo(insumo)
    suspend fun updateInsumo(id: Int, insumo: InsumoDto): InsumoDto = insumoApi.updateInsumo(id, insumo)
    suspend fun deleteInsumo(id: Int) = insumoApi.deleteInsumo(id)

    // InsumoDetalle
    suspend fun getInsumoDetalles(): List<InsumoDetalleDto> = insumoDetalleApi.getInsumoDetalles()
    suspend fun getInsumoDetalle(id: Int): InsumoDetalleDto = insumoDetalleApi.getInsumoDetalle(id)
    suspend fun createInsumoDetalle(detalle: InsumoDetalleDto): InsumoDetalleDto = insumoDetalleApi.createInsumoDetalle(detalle)
    suspend fun updateInsumoDetalle(id: Int, detalle: InsumoDetalleDto): InsumoDetalleDto = insumoDetalleApi.updateInsumoDetalle(id, detalle)
    suspend fun deleteInsumoDetalle(id: Int) = insumoDetalleApi.deleteInsumoDetalle(id)

    // Reclamo
    suspend fun getReclamos(): List<ReclamoDto> = reclamoApi.getReclamos()
    suspend fun getReclamo(id: Int): ReclamoDto = reclamoApi.getReclamo(id)
    suspend fun createReclamo(reclamo: ReclamoDto): ReclamoDto = reclamoApi.createReclamo(reclamo)
    suspend fun updateReclamo(id: Int, reclamo: ReclamoDto): ReclamoDto = reclamoApi.updateReclamo(id, reclamo)
    suspend fun deleteReclamo(id: Int) = reclamoApi.deleteReclamo(id)
}

// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
// import axios from 'axios';
enum URL {
    listUserCompany = '/api/company/listUserCompany',
    create = '/api/company/create',
    update = '/api/company/update',
}

const listUserCompanyApi = async (params: any) => get<any>({ url: URL.listUserCompany, params: params, data: {}, headers: {} });
const createApi = async (data: any) =>
    post<any>({ url: URL.create, params: {}, data: data, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const updateApi = async (data: any) =>
    post<any>({ url: URL.update,params: {}, data: data, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });

export { listUserCompanyApi, createApi, updateApi };

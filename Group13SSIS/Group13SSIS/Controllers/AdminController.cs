using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using Group13SSIS.Utility;

namespace Group13SSIS.Controllers
{
    public class AdminController : Controller
    {
        public ActionResult Index()
        {
            
            return View();
        }


        public ActionResult UserList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var userlist = db.Users.ToList();
                var uservmlist = new List<UserVM>();
                foreach (var user in userlist)
                {
                    if(user.Status == "Activated")
                    {
                        uservmlist.Add(new UserVM()
                        {
                            UserId = user.UserId,
                            Username = user.Username,
                            Password = user.Password,
                            Name = user.Name,
                            Email = user.Email,
                            DeptId = db.Depts.Where(x => x.DeptId == user.DeptId).FirstOrDefault().Name,
                            RoleId = db.Roles.Where(x => x.RoleId == user.RoleId).FirstOrDefault().Name
                        });
                    }
                }
                ViewData["userlist"] = uservmlist;
            }
            return View();
        }
        [HttpGet]
        public ActionResult CreateUser()
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.Where(x => x.Status == "Activated").ToList();
                ViewData["roles"] = dc.Roles.ToList();
            }
            return View();
        }
        [HttpPost]
        public ActionResult CreateUser(UserVM userVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.Where(x => x.Status == "Activated").ToList();
                ViewData["roles"] = dc.Roles.ToList();
            }
            if (ModelState.IsValid)
            {
                if (UsernameVerification.IsUsernameExist(userVM.Username))
                {
                    ModelState.AddModelError("Username", "Username already exist");
                    return View(userVM);
                }
                User user = new User()
                {
                    Username = userVM.Username,
                    Password = Crypto.Hash(userVM.Password),
                    Name = userVM.Name,
                    Email = userVM.Email,
                    DeptId = Int32.Parse(userVM.DeptId),
                    RoleId = Int32.Parse(userVM.RoleId),
                    Status = "Activated"
                };
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Users.Add(user);
                    db.SaveChanges();
                    return RedirectToAction("UserList");
                }
            }
            return View();
        }
        [HttpGet]
        public ActionResult EditUser(int id)
        {
            var userVM = new UserVM();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                ViewData["depts"] = db.Depts.Where(x => x.Status == "Activated").ToList();
                ViewData["roles"] = db.Roles.ToList();
                var user = db.Users.Where(x => x.UserId == id).FirstOrDefault();
                userVM.Username = user.Username;
                userVM.Name = user.Name;
                userVM.Email = user.Email;
                ViewBag.DeptId = user.DeptId;
                ViewBag.RoleId = user.RoleId;
            }
            return View(userVM);
        }
        [HttpPost]
        public ActionResult EditUser(int id, UserVM userVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.Where(x => x.Status == "Activated").ToList();
                ViewData["roles"] = dc.Roles.ToList();
                ViewBag.DeptId = Int32.Parse(userVM.DeptId);
                ViewBag.RoleId = Int32.Parse(userVM.RoleId);
            }
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    var user = db.Users.Where(x => x.UserId == id).FirstOrDefault();
                    user.Username = userVM.Username;
                    user.Name = userVM.Name;
                    user.Email = userVM.Email;
                    user.Password = Crypto.Hash(userVM.Password);
                    user.RoleId = Int32.Parse(userVM.RoleId);
                    user.DeptId = Int32.Parse(userVM.DeptId);
                    db.SaveChanges();
                }
                return RedirectToAction("UserList");
            }
            return View(userVM);
        }
        public ActionResult DeleteUser(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var user = db.Users.Where(x => x.UserId == id).FirstOrDefault();
                user.Status = "Expired";
                db.SaveChanges();
            }
            return RedirectToAction("UserList");
        }


        public ActionResult DeptList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var deptlist = db.Depts.ToList();
                var deptvmlist = new List<DeptVM>();
                foreach (var dept in deptlist)
                {
                    if (dept.Status == "Activated")
                    {
                        DeptVM deptVM = new DeptVM();
                        deptVM.DeptId = dept.DeptId;
                        deptVM.Code = dept.Code;
                        deptVM.Name = dept.Name;
                        deptVM.ContactName = dept.ContactName;
                        deptVM.Tel = dept.Tel;
                        deptVM.FaxNo = dept.FaxNo;
                        if (dept.HeadId != null) deptVM.HeadId = db.Users.Where(x => x.UserId == dept.HeadId).FirstOrDefault().Name;
                        if (dept.RepId != null) deptVM.RepId = db.Users.Where(x => x.UserId == dept.RepId).FirstOrDefault().Name;
                        if (dept.PointId != null) deptVM.PointId = db.CollectionPoints.Where(x => x.PointId == dept.PointId).FirstOrDefault().Name;
                        deptvmlist.Add(deptVM);
                    }
                }
                ViewData["deptlist"] = deptvmlist;
            }
            return View();
        }
        [HttpGet]
        public ActionResult CreateDept()
        {
            return View();
        }
        [HttpPost]
        public ActionResult CreateDept(DeptVM deptVM)
        {
            if(ModelState.IsValid)
            {
                Dept dept = new Dept()
                {
                    Code = deptVM.Code,
                    Name = deptVM.Name,
                    ContactName = deptVM.ContactName,
                    Tel = deptVM.Tel,
                    FaxNo = deptVM.FaxNo,
                    Status = "Activated"
                };
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Depts.Add(dept);
                    db.SaveChanges();
                    return RedirectToAction("DeptList");
                }
            }
            return View();
        }
        [HttpGet]
        public ActionResult EditDept(int id)
        {
            DeptVM deptVM = new DeptVM();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                ViewData["heads"] = db.Users.Where(x => x.RoleId == 3 && x.DeptId == id && x.Status == "Activated").ToList();
                ViewData["reps"] = db.Users.Where(x => x.RoleId == 2 && x.DeptId == id && x.Status == "Activated").ToList();
                ViewData["points"] = db.CollectionPoints.Where(x => x.Status == "Activated").ToList();
                var dept = db.Depts.Where(x => x.DeptId == id).FirstOrDefault();
                deptVM.Code = dept.Code;
                deptVM.Name = dept.Name;
                deptVM.ContactName = dept.ContactName;
                deptVM.Tel = dept.Tel;
                deptVM.FaxNo = dept.FaxNo;
                ViewBag.HeadId = dept.HeadId;
                ViewBag.RepId = dept.RepId;
                ViewBag.PointId = dept.PointId;
            }
            return View(deptVM);
        }
        [HttpPost]
        public ActionResult EditDept(int id, DeptVM deptVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["heads"] = dc.Users.Where(x => x.RoleId == 3 && x.DeptId == id && x.Status == "Activated").ToList();
                ViewData["reps"] = dc.Users.Where(x => x.RoleId == 2 && x.DeptId == id && x.Status == "Activated").ToList();
                ViewData["points"] = dc.CollectionPoints.Where(x => x.Status == "Activated").ToList();
                if (deptVM.HeadId != null) ViewBag.HeadId = Int32.Parse(deptVM.HeadId);
                if (deptVM.RepId != null) ViewBag.RepId = Int32.Parse(deptVM.RepId);
                if (deptVM.PointId != null) ViewBag.PointId = Int32.Parse(deptVM.PointId);
            }
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    var dept = db.Depts.Where(x => x.DeptId == id).FirstOrDefault();
                    dept.Code = deptVM.Code;
                    dept.Name = deptVM.Name;
                    dept.ContactName = deptVM.ContactName;
                    dept.Tel = deptVM.Tel;
                    dept.FaxNo = deptVM.FaxNo;
                    if (deptVM.HeadId != null) dept.HeadId = Int32.Parse(deptVM.HeadId);
                    else dept.HeadId = null;
                    if (deptVM.RepId != null)  dept.RepId = Int32.Parse(deptVM.RepId);
                    else dept.RepId = null;
                    if (deptVM.PointId != null)  dept.PointId = Int32.Parse(deptVM.PointId);
                    else dept.PointId = null;
                    db.SaveChanges();
                }
                return RedirectToAction("DeptList");
            }
            return View();
        }
        public ActionResult DeleteDept(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var dept = db.Depts.Where(x => x.DeptId == id).FirstOrDefault();
                dept.Status = "Expired";
                db.SaveChanges();
            }
            return RedirectToAction("DeptList");
        }
    }
}